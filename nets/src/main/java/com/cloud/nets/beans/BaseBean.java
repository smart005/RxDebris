package com.cloud.nets.beans;

public class BaseBean {
    /**
     * 返回code=200成功
     */
    private String code = "";

    /**
     * api返回消息
     */
    private String message = "";
    /**
     * 是否有下一页
     */
    private boolean hasNextPage = false;
    /**
     * 是否有上一页
     */
    private boolean hasPreviousPage = false;
    /**
     * 是否第一页
     */
    private boolean isFirstPage = false;
    /**
     * 是否最后一页
     */
    private boolean isLastPage = false;
    /**
     * 第一页索引
     */
    private int firstPage = 0;
    /**
     * 最后一页索引
     */
    private int lastPage = 0;
    /**
     * 当前页索引
     */
    private int pageNum = 0;
    /**
     * 每一页大小
     */
    private int pageSize = 0;
    /**
     * 总页数
     */
    private int pages = 0;
    /**
     * 总记录数
     */
    private int total = 0;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean isFirstPage() {
        return isFirstPage;
    }

    public void setFirstPage(boolean firstPage) {
        isFirstPage = firstPage;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public void setLastPage(boolean lastPage) {
        isLastPage = lastPage;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
