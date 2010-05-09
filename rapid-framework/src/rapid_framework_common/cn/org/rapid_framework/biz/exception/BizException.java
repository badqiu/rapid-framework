package cn.org.rapid_framework.biz.exception;

import cn.org.rapid_framework.exception.ActionException;
import cn.org.rapid_framework.exception.ErrorCode;

public class BizException extends ActionException{

	public BizException(ErrorCode errorCode, Throwable cause) {
		super(errorCode, cause);
	}

	public BizException(ErrorCode errorEnum) {
		super(errorEnum);
	}

	public BizException(String action, ErrorCode errorCode, Throwable cause) {
		super(action, errorCode, cause);
	}

	public BizException(String action, ErrorCode errorCode) {
		super(action, errorCode);
	}

}
