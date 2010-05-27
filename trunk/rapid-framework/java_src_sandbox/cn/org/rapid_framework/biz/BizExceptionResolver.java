package cn.org.rapid_framework.biz;

import cn.org.rapid_framework.exception.ErrorCode;

public interface BizExceptionResolver {
	
	public ErrorCode resoverException(Exception e) ;
	
}
