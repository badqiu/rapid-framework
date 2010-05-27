package cn.org.rapid_framework.biz;

import cn.org.rapid_framework.exception.ErrorCode;
import cn.org.rapid_framework.exception.ErrorCodeException;
import cn.org.rapid_framework.exception.ErrorCodeImpl;


public class DefaultBizExceptionResolver implements BizExceptionResolver {

	public ErrorCode resoverException(Exception e) {
		if(e instanceof ErrorCodeException) {
			return ((ErrorCodeException)e).getErrorCode();
		}else if(e instanceof IllegalArgumentException) {
			return new ErrorCodeImpl("IllegalArgumentException",e.toString());
		}else if(e instanceof IllegalStateException) {
			return new ErrorCodeImpl("IllegalStateException",e.toString());
		}else {
			return new ErrorCodeImpl("SystemUnknowError",e.toString());
		}
	}

}
