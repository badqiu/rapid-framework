
package cn.org.rapid_framework.util.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 
 * 包含“分页”信息的List
 * 
 * <p>要得到总页数请使用 toPaginator().getTotalPages();</p>
 * 
 * @author badqiu
 */
public class PageList<E> extends ArrayList<E> implements Serializable {

    private static final long serialVersionUID = 1412759446332294208L;
    
    /** 分页大小 */
    private int               pageSize;
    /** 页数  */
    private int               page;
    /** 总记录数 */
    private int               totalItems;

    public PageList() {}
    
	public PageList(Collection<? extends E> c) {
		super(c);
	}

	public PageList(int page, int pageSize, int totalItems) {
        this.page = page;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
    }
	
	public PageList(Collection<? extends E> c,int page, int pageSize, int totalItems) {
        super(c);
        this.page = page;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
    }

    public PageList(Paginator p) {
        this.page = p.getPage();
        this.pageSize = p.getPageSize();
        this.totalItems = p.getTotalItems();
    }
    
    public PageList(Collection<? extends E> c,Paginator p) {
        super(c);
        this.page = p.getPage();
        this.pageSize = p.getPageSize();
        this.totalItems = p.getTotalItems();
    }
    
    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

	public int getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}
	
	/**
	 * 得到分页器，通过Paginator可以得到总页数等值
	 * @return
	 */
	public Paginator getPaginator() {
		return new Paginator(page,pageSize,totalItems);
	}
	
}
