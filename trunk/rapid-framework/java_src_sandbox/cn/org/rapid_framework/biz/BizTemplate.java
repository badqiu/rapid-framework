package cn.org.rapid_framework.biz;


public class BizTemplate {
//	final static Log log = LogFactory.getLog(BizTemplate.class);
//	
//	private BizExceptionResolver bizExceptionResolver = new DefaultBizExceptionResolver();
//	
//	public BizTemplate(){
//	}
//	
//	public BizTemplate(BizExceptionResolver bizExceptionResolver) {
//		super();
//		this.bizExceptionResolver = bizExceptionResolver;
//	}
//
//	public BizExceptionResolver getBizExceptionResolver() {
//		return bizExceptionResolver;
//	}
//
//	public void setBizExceptionResolver(BizExceptionResolver bizExceptionResolver) {
//		this.bizExceptionResolver = bizExceptionResolver;
//	}
//
//	public <T extends WSResult>T execute(T result,BizCommand cmd) {
//		try {
//			onBeforeExecCommand(result);
//			cmd.execute();
//			onAfterExecCommand(result);
//		} catch(Exception e) {
//			resolveException(result, e);
//			onException(result, e);
//		} finally {
//			onCompletedExecCommand();
//		}
//		return (T)result;
//	}
//	
//	protected void onCompletedExecCommand() {
//	}
//
//	protected <T extends WSResult> void onBeforeExecCommand(T result) {
//	}
//	
//	protected <T extends WSResult> void onAfterExecCommand(T result) {
//	}
//
//	protected <T extends WSResult> void onException(T result, Exception e) {
//	}
//
//	private <T extends WSResult> void resolveException(T result, Exception e) {
//		ErrorCode code = bizExceptionResolver.resoverException(e);
//		if(code == null) {
//			String errorInfo = "resoverException for errorCode fail,bizExceptionResover:"+bizExceptionResolver+" exceptionClass:"+e.getClass()+" exceptionCause:"+e;
//			if(log.isErrorEnabled()) {
//				log.error(errorInfo,e);
//			}
//			throw new IllegalStateException(errorInfo);
//		}
//		result.setErrorCode(code);
//	}
}
