package cn.org.rapid_framework.biz;

import cn.org.rapid_framework.exception.ErrorCode;

public interface BizExceptionResover {
	
	public ErrorCode resoverException(Exception e) ;
	
}
