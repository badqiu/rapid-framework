package cn.org.rapid_framework.biz;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.company.project.facade.dto.result.iface.WSResult;

import cn.org.rapid_framework.exception.ActionException;
import cn.org.rapid_framework.exception.ErrorCode;
import cn.org.rapid_framework.exception.ErrorCodeException;
import cn.org.rapid_framework.exception.ErrorCodeImpl;

public class BizTemplate {
	final static Log log = LogFactory.getLog(BizTemplate.class);
	
	private BizExceptionResover bizExceptionResover;
	
	public <T extends WSResult>T execute(T result,BizCommand cmd) {
		try {
			cmd.execute();
		} catch(Exception e) {
			ErrorCode code = bizExceptionResover.resoverException(e);
			if(code == null) {
				if(log.isErrorEnabled()) {
					log.error("resoverException for errorCode fail,bizExceptionResover:"+bizExceptionResover);
				}
				throw new IllegalStateException("resoverException for errorCode fail,bizExceptionResover:"+bizExceptionResover);
			}
			result.setErrorCode(code);
		}
		return (T)result;
	}
}
