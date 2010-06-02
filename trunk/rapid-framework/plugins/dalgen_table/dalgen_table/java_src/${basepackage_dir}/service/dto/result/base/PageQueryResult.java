package com.company.project.service.dto.result.base;


public class PageQueryResult<T extends java.io.Serializable> extends QueryResult<T> {

	int pageNo;
	int pageSize;
	int totalCount;
	
	public PageQueryResult() {
	}
	
	public PageQueryResult(int pageNo, int pageSize, int totalCount) {
		super();
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

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}
