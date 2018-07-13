# code-generator

使用Thymeleaf模版，根据Mysql数据库中的表结构生成对应的增删改查，包括分页功能

`如果需要对其他数据库分页，需修改对应的查询，后续我会统一接口，默认是对Mysql的实现，可通过实现接口，实现其他数据库表的生成
`





分页功能使用的mybatis实现，代码后续会补充上来

架构
springboot+jdbc  

后续会慢慢优化

Thymeleaf Generator mybatis  CRUD   

目前只是初略的完成DAO接口，service接口，DAO的映射文件，Servcie实现及VO对象的实现

注意：生成的代码是带了分页功能，需要配合代码中的mybatis分页插件使用，不然你懂得....

具体代码请参照目录下的分页插件

这里说明一下分页插件的实现：其实原理很简单就是对查询的SQL进行拦截，如果拦截到参数中带有PageVO对象，就先查询对应count，如果count有数据就继续查数据列表，注意数据是分页的
至于Count的查询不要写接口，但是Mapper的映射文件里要写查count的接口，通过这种方式实现比较灵活，不管是什么数据，自己写好实现就可以，具体代码看看代码，这里就不贴出来


2018-07-04 修改默认默认配置
默认输出的报名是com.example.it 
默认输出的模块是demo
默认查找的数据库是test

如果的上述默认配置进行修改，在spring配置文件中修改称自己的配置即可，如下：
```
#配置对应的代码生成目标路径
code.generator.target.base-package=com.example.velocitydemo
code.generator.target.module=user

#目标数据库
code.generator.target.targetDatabase=blog
```

修改了支持自己定义的数据库，默认只是对Mysql数据库的实现，如果想实现其他数据库，只需实现ITableService接口，具体请参照MySqlTableService
```
package com.anjiel.it.code.service.impl;

import com.anjiel.it.code.entity.Entity;
import com.anjiel.it.code.entity.Field;
import com.anjiel.it.code.service.ITableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 查询mysqlTable表信息
 *
 * @author liuhao
 */
public class MySqlTableService implements ITableService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据数据名，表名查询表信息（包括字段信息）
     * @param scheme  数据库名称
     * @param tableName
     * @return
     */
    @Override
    public List<Entity> findTableListBySchema(String scheme,String tableName) {

        String sql = "SELECT TABLE_SCHEMA,TABLE_NAME,TABLE_COMMENT FROM `TABLES` where TABLE_SCHEMA=? and table_name = ?";
        List<Entity> entityList = jdbcTemplate.query(sql, preparedStatement -> {
            preparedStatement.setString(1, scheme);
            preparedStatement.setString(2, tableName);}
                ,
                (resultSet, i) -> {

            Entity entity = new Entity();
            entity.setTableName(resultSet.getString(2));
            entity.setTableComments(resultSet.getString(3));
            //查询字段详情，构建VO和Mapper.xml时使用
            List<Field> fieldList = findColumnListByTable(scheme,tableName);
            entity.setDisplayFields(fieldList);
            return entity;
        });

        return entityList;
    }

    private List<Field> findColumnListByTable(String schame, String table) {

        String sql="SELECT COLUMN_NAME,COLUMN_TYPE,IS_NULLABLE,DATA_TYPE,COLUMN_KEY,COLUMN_COMMENT " +
                    "FROM `COLUMNS` " +
                    "WHERE TABLE_SCHEMA=? AND TABLE_NAME=?";

        return jdbcTemplate.query(sql, (PreparedStatementSetter) preparedStatement -> {
            preparedStatement.setString(1, schame);
            preparedStatement.setString(2, table);
        }, (resultSet, i) -> {
            Field field =new Field();
            field.setColumnName(resultSet.getString(1));
            field.setColumnType(resultSet.getString(2));
            field.setNullAble(resultSet.getString(3));
            field.setDataType(resultSet.getString(4));
            field.setNullAble(resultSet.getString(5));
            field.setComments(resultSet.getString(6));
            return field;
        });
    }

}

```
我的想法是做称一个插件之类的，通过配置一个模版，可能制动生成代码，由于本人对插件开发不是很熟悉，一直做web开发，所以就想着做一个web的生成代码的。

目前直接运行单元测试即可，前端还没有完成，后续会陆续优化
```
 @Test
    public void testGeneratorFile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/generator")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
```

最终的执行效果如下：
![result](https://github.com/liuhao0813/code-generator/blob/master/atta/result.png "运行结果如下")



2018-07-13 修改日志

以上模版都是针对MYSQL写的，如果需要支持其他数据库，可能在mybatis对应的映射模板文件中做调整，比如说分页功能,你需要修改成对应的数据的分页
mysql如下：
```$xslt
<select id="findPaged[( ${entity.entityName} )]List" resultType="[(${basePackage})].[(${module})].entity.[( ${entity.entityName} )]VO">
        <include refid="queryBaseInfo"></include>
        <include refid="queryPagedCondition"></include>
        limit #{pageVO.startIndex},#{pageVO.endIndex}
    </select>
```

注意：为了更好的应用于生产环境，这里需要对每个表添加四个审计字段，
create_by 创建人
create_time 创建时间
update_by 修改人
update_time 修改时间

1、添加这个四个字段，一般在生成环境用得上
2、处理模版起来比较方便，不用考虑迭代情况下最后一个逗号问题
问题如下：
```$xslt
<sql id="queryBaseInfo">
        SELECT 
        [# th:each="field : ${entity.displayFields}"]
        [( ${field.columnName} )] as [( ${field.fieldName} )], 
        [/]
        <!-- 如果没有如下字段，上面迭代出来的字段后面就会多一个逗号  -->
        create_by as createBy,
        create_time as createTime,
        update_by as updateBy,
        update_time as updateTime
        FROM
        [( ${entity.tableName} )]
    </sql>
```
> 如果不需要的同学请自行处理一下，有好的处理方案欢迎交流



#### 如在使用过程中有任何问题欢迎联系我QQ：237594169 也可以直接在GITHUB上提ISSUES。功能已经实现，后续会做代码的重构和优化