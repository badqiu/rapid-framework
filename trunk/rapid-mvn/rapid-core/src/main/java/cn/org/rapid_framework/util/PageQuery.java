package cn.org.rapid_framework.util;
/**
 * 分页查询对象
 * @author zhongxuan
 * @version $Id: PageQuery.java,v 0.1 2010-11-29 下午05:34:12 zhongxuan Exp $
 */
public class PageQuery implements java.io.Serializable{
    private static final long serialVersionUID = -8000900575354501298L;

    private int    page;
    private int    pageSize;

    public PageQuery() {
    }

    public PageQuery(PageQuery query) {
        this.page = query.page;
        this.pageSize = query.pageSize;
    }
    
    public PageQuery(int pageNo, int pageSize) {
        this.page = pageNo;
        this.pageSize = pageSize;
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

}
