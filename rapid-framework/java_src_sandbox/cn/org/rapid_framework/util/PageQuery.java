package cn.org.rapid_framework.util;

public class PageQuery implements java.io.Serializable{
    private static final long serialVersionUID = -8000900575354501298L;

    private int    page;
    private int    pageSize;
    private String orderBy;

    public PageQuery() {
    }

    public PageQuery(PageQuery query) {
        this.page = query.page;
        this.orderBy = query.orderBy;
        this.pageSize = query.pageSize;
    }
    
    public PageQuery(int pageNo, int pageSize, String orderBy) {
        this.page = pageNo;
        this.pageSize = pageSize;
        this.orderBy = orderBy;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int pageNo) {
        this.page = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

}
