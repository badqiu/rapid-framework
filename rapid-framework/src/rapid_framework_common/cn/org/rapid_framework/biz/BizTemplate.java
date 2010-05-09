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
	
	private BizExceptionResolver bizExceptionResolver = new DefaultBizExceptionResolver();
	
	public BizTemplate(){
	}
	
	public BizTemplate(BizExceptionResolver bizExceptionResolver) {
		super();
		this.bizExceptionResolver = bizExceptionResolver;
	}

	public BizExceptionResolver getBizExceptionResolver() {
		return bizExceptionResolver;
	}

	public void setBizExceptionResolver(BizExceptionResolver bizExceptionResolver) {
		this.bizExceptionResolver = bizExceptionResolver;
	}

	public <T extends WSResult>T execute(T result,BizCommand cmd) {
		try {
			beforeExecCommand(result);
			cmd.execute();
			afterExecCommand(result);
		} catch(Exception e) {
			handleException(result, e);
		}
		return (T)result;
	}
	
	protected <T extends WSResult> void beforeExecCommand(T result) {
	}
	
	protected <T extends WSResult> void afterExecCommand(T result) {
	}

	private <T extends WSResult> void handleException(T result, Exception e) {
		resolveException(result, e);
	}

	protected <T extends WSResult> void resolveException(T result, Exception e) {
		ErrorCode code = bizExceptionResolver.resoverException(e);
		if(code == null) {
			String errorInfo = "resoverException for errorCode fail,bizExceptionResover:"+bizExceptionResolver+" exceptionClass:"+e.getClass()+" exceptionCause:"+e;
			if(log.isErrorEnabled()) {
				log.error(errorInfo,e);
			}
			throw new IllegalStateException(errorInfo);
		}
		result.setErrorCode(code);
	}
}
