/**
 * Alipay.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package cn.org.rapid_framework.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 
 * 包含“分页”信息的List
 * 
 * @author badqiu
 */
public class PageList<E> extends ArrayList<E> implements Serializable {

    private static final long serialVersionUID = 1412759446332294208L;
    
    private int               pageSize;
    private int               pageNo;
    private long              totalItems;

    public PageList() {}
    
	public PageList(Collection<? extends E> c) {
		super(c);
	}

	public PageList(int pageNo, int pageSize, long totalItems) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
    }
	
	public PageList(Collection<? extends E> c,int pageNo, int pageSize, long totalItems) {
        super(c);
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
    }

    public PageList(Paginator p) {
        this.pageNo = p.getPageNo();
        this.pageSize = p.getPageSize();
        this.totalItems = p.getTotalItems();
    }
    
    public PageList(Collection<? extends E> c,Paginator p) {
        super(c);
        this.pageNo = p.getPageNo();
        this.pageSize = p.getPageSize();
        this.totalItems = p.getTotalItems();
    }
    
    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

	public long getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(long totalItems) {
		this.totalItems = totalItems;
	}

	public Paginator toPaginator() {
		Paginator p = new Paginator();
		p.setPageNo(pageNo);
		p.setPageSize(pageSize);
		p.setTotalItems(totalItems);
		return p;
	}
	
}
