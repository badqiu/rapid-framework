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
 * 包含“分页”信息的List,实现了序列化，支持泛型
 * 
 * @author badqiu
 */
public class PageList<E> extends ArrayList<E> implements Serializable {

    private static final long serialVersionUID = 1412759446332294208L;
    private int               pageSize;
    private int               pageNo;
    private long              totalCount;

    public PageList() {}

    public PageList(Collection<E> c,PageList pageList) {
        super(c);
        this.pageSize = pageList.getPageSize();
        this.pageNo = pageList.getPageNo();
        this.totalCount = pageList.getTotalCount();
    }
    
    public PageList(Collection<E> c,int pageSize, int pageNo, long totalCount) {
        super(c);
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        this.totalCount = totalCount;
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

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

}
