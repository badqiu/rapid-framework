package com.company.project.facade.dto.result.iface;

import cn.org.rapid_framework.exception.ErrorCode;

public interface WSResult {
	/*
	 * 通用错误: 
	 * 参数错误
	 * 未知系统内部错误,系统内部错误,,数据库错误
	 */
	public boolean isError();
	
	public ErrorCode getErrorCode();
	
	public void setErrorCode(ErrorCode code);
	
}
