package com.company.project.facade.dto.result.iface.impl;

import com.company.project.facade.dto.result.iface.PageQueryResult;

import cn.org.rapid_framework.exception.ErrorCode;

public class PageQueryResultImpl extends WSResultImpl implements PageQueryResult {

	int pageNo;
	int pageSize;
	int totalCount;
	
	public PageQueryResultImpl() {
	}
	
	public PageQueryResultImpl(int pageNo, int pageSize, int totalCount) {
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
