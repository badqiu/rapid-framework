package cn.org.rapid_framework.util;

public class PageQuery implements java.io.Serializable{
    private static final long serialVersionUID = -8000900575354501298L;

    private int    pageNo;
    private int    pageSize;
    private String orderBy;

    public PageQuery() {
    }

    public PageQuery(int pageNo, int pageSize, String orderBy) {
        super();
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.orderBy = orderBy;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
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
