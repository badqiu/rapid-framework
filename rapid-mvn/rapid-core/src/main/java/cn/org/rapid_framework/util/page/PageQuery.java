package cn.org.rapid_framework.util.page;
/**
 * 分页查询对象
 * @author badqiu
 * @version $Id: PageQuery.java,v 0.1 2010-11-29 下午05:34:12 badqiu Exp $
 */
public class PageQuery implements java.io.Serializable{
    private static final long serialVersionUID = -8000900575354501298L;
    
    public static final int DEFAULT_PAGE_SIZE = 10;
    /** 页数  */
    private int    page;
    /** 分页大小 */
    private int    pageSize = DEFAULT_PAGE_SIZE;

    public PageQuery() {
    }

    public PageQuery(int pageSize) {
        this.pageSize = pageSize;
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

    public String toString() {
    	return "page:"+page+",pageSize:"+pageSize;
    }
    
}
