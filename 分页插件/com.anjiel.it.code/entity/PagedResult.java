package com.anjiel.it.code.entity;

import java.util.List;


/**
 *  分页查找返回的信息
 *
 * @param <T>
 */
public class PagedResult<T> {


    /**
     * 分页对象
     */
    private PageVO pageVO;


    /**
     * 结果集
     */
    private List<T> result;



    public PageVO getPageVO() {
        return pageVO;
    }

    public void setPageVO(PageVO pageVO) {
        this.pageVO = pageVO;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }
}
