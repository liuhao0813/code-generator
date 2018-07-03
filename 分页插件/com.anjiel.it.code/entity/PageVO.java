package com.anjiel.it.code.entity;


/**
 * 分页对象封装
 *
 * @author liuhao
 * @created 2018-6-29
 */
public class PageVO extends BaseVO{

    /**
     * 每页大小 默认每页展示15条数据
     */
    private int pageSize = 15;

    /**
     * 当前页码 默认是第一页
     */
    private int currentPage=1;
    /**
     * 起始行号
     */
    private int startIndex;

    /**
     * 结束行号
     */
    private int endIndex;
    /**
     * 总记录输
     */
    private int total;


    private int resultModel = 0;


    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getStartIndex() {
        if(currentPage<1){
            this.currentPage=1;
        }
        return (this.currentPage-1)*pageSize;
    }

    public void setStartIndex(int startIndex) {

        this.startIndex = startIndex;
    }

    public int getEndIndex() {

        return this.startIndex+this.pageSize;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getResultModel() {
        return resultModel;
    }

    public void setResultModel(int resultModel) {
        this.resultModel = resultModel;
    }

    public int getPageTotal(){
        if(this.total%pageSize>0){
            return this.total/pageSize+1;
        }else{
            return this.total/pageSize;
        }
    }
}
