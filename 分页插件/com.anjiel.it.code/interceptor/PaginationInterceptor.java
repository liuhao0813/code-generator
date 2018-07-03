package com.anjiel.it.code.interceptor;

import com.example.mybatis.demo.entity.PageVO;
import com.example.mybatis.demo.entity.PagedResult;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.logging.jdbc.ConnectionLogger;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Mybatis的分页插件，支持任何一种关系型数据库，主要按照约定进行
 *
 * @author liuhao
 *
 */
@Component
@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class PaginationInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(PaginationInterceptor.class);

    private static final Log statementLog = LogFactory.getLog(PaginationInterceptor.class);

    //以下四个是对应的参数索引
    private static final int MAPPED_STATEMENT_INDEX = 0;
    private static final int PARAMETER_INDEX = 1;
    private static final int ROW_BOUNDS_INDEX = 2;
    private static final int RESULT_HANDLER_INDEX = 3;


    //分页查询统计数据查询对应的后缀信息
    private static final String SQL_COUNT_ID_SUFFIX = "Count";

    //查询所有数据，包括结果集和记录数
    private static final int PAGE_RESULT_MODEL_ALL = 0;

    //查询模式为只查询统计的总记录数
    private static final int PAGE_RESULT_MODEL_COUNT = 1;
    //查询模式为只查询结果集
    private static final int PAGE_RESULT_MODEL_LIST = 2;


    /**
     * 主要的拦截方法，对所有的查询进行拦截
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        //取得所有的参数信息
        Object[] queryArgs = invocation.getArgs();

        //从参数中提取对应的信息具体对应的信息参照args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
        MappedStatement mappedStatement = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
        String sqlId = mappedStatement.getId();
        Object parameter = queryArgs[PARAMETER_INDEX];

        //检查参数中是否有pageVO，没有就无需拦截
        PageVO pageVO = checkInvocation(sqlId, parameter);
        if (null == pageVO) {
            return invocation.proceed();
        }

        Executor executor = (Executor) invocation.getTarget();

        //判断pageVO中的查询模式，是否需要查询count信息，同时pageVO的total是否为0 或者查询模式为只查询统计的条数
        if (((pageVO.getResultModel() == PAGE_RESULT_MODEL_ALL) && (pageVO.getTotal() == 0)) || (pageVO.getResultModel() == PAGE_RESULT_MODEL_COUNT)) {
            queryCount(queryArgs, mappedStatement, sqlId, pageVO, executor);
        }

        List resultList = null;
        //查询模式同上，如果total大于0才进行分页查询
        if ((pageVO.getResultModel()==PAGE_RESULT_MODEL_ALL)&&(pageVO.getTotal()> 0)||(pageVO.getResultModel()==PAGE_RESULT_MODEL_LIST)){
            resultList=queryResultList(executor,mappedStatement,queryArgs);
            if (resultList.size()>pageVO.getTotal()) {
                pageVO.setTotal(resultList.size());
            }
        }

        //构建返回对象
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPageVO(pageVO);
        pagedResult.setResult(resultList);


        //所有的返回对象需以List的方式进行返回，所有这个重新包装了一下
        List<PagedResult> pagedResultList = new ArrayList<>();
        pagedResultList.add(pagedResult);

        return pagedResultList;
    }

    /**
     * 统计查询
     * @param queryArgs
     * @param mappedStatement
     * @param sqlId
     * @param pageVO
     * @param executor
     */
    private void queryCount(Object[] queryArgs, MappedStatement mappedStatement, String sqlId, PageVO pageVO, Executor executor) {

        String sqlCount = sqlId + SQL_COUNT_ID_SUFFIX;
        Configuration configuration = mappedStatement.getConfiguration();
        MappedStatement statement = configuration.getMappedStatement(sqlCount);

        List<Object> list = queryResultList(executor, statement, queryArgs);
        pageVO.setTotal((int) list.get(0));

    }


    /**
     * 执行对应的结果集查询
     *
     * @param executor
     * @param mappedStatement
     * @param queryArgs
     * @return
     */
    private List<Object> queryResultList(Executor executor, MappedStatement mappedStatement, Object[] queryArgs) {

        Object parameter = queryArgs[PARAMETER_INDEX];

        RowBounds rowBounds = (RowBounds) queryArgs[ROW_BOUNDS_INDEX];
        ResultHandler resultHandler = (ResultHandler) queryArgs[RESULT_HANDLER_INDEX];

        Configuration configuration = mappedStatement.getConfiguration();
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        StatementHandler statementHandler = configuration.newStatementHandler(executor, mappedStatement, parameter, rowBounds, resultHandler, boundSql);

        Statement statement = null;
        List query = null;
        try {
            statement = prepareStatement(executor, statementHandler);
            query = statementHandler.query(statement, resultHandler);
        } catch (SQLException e) {
            tryStatementClose(statement);
            logger.info("查询数据异常，请检查：{}", e);
        } finally {
            tryStatementClose(statement);
        }
        return query;

    }


    /**
     * 关闭statement对象
     * @param statement
     */
    private void tryStatementClose(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通过mybatis的ConnectionLogger对象获取对应的Connection连接，并取得对应的Statememnt对象返回
     * @param executor
     * @param statementHandler
     * @return
     * @throws SQLException
     */
    private Statement prepareStatement(Executor executor, StatementHandler statementHandler) throws SQLException {

        Connection connection = ConnectionLogger.newInstance(executor.getTransaction().getConnection(), statementLog, 1);

        Statement statement = null;
        try {
            statement = statementHandler.prepare(connection, 300);
            statementHandler.parameterize(statement);
        } catch (SQLException e) {
            tryStatementClose(statement);
        }

        return statement;

    }

    /**
     * 判断对应的sqlId查询中的参数是否有PageVO对象，有则继续，没有就不进行拦截
     * @param sqlId
     * @param parameter
     * @return
     */
    private PageVO checkInvocation(String sqlId, Object parameter) {
        if (null == parameter) {
            return null;
        }

        if (null != sqlId && !sqlId.endsWith(SQL_COUNT_ID_SUFFIX)) {
            return findPageVO(parameter);
        }

        return null;
    }


    /**
     * 在参数中查找是否存在PageVO对象
     *
     * @param parameter
     * @return
     */
    private PageVO findPageVO(Object parameter) {

        if (parameter instanceof Map) {
            Map<Object, Object> map = (Map) parameter;
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                if (entry.getValue() instanceof PageVO) {
                    return (PageVO) entry.getValue();
                }
            }
        }
        if (parameter instanceof PageVO) {
            return (PageVO) parameter;
        }

        return null;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
