package com.company.project.service.dto.result.base;

import java.util.List;



public class PageQueryResult<T extends java.io.Serializable> extends QueryResult<T> {
	private static final long serialVersionUID = 5058725391401295309L;
	
    int pageNo;
	int pageSize;
	long totalCount;
	
	public PageQueryResult() {
	}
	
	public PageQueryResult(String errorCode,String errorDetails) {
        super(errorCode, errorDetails);
    }

    public PageQueryResult(List<T> itemList,int pageNo, int pageSize, long totalCount) {
		super(itemList);
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.totalCount = totalCount;
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

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

}
