package cn.org.rapid_framework.exception;

/**
 * 用于业务方法的异常
 * @author badqiu
 *
 */
public class ActionException extends ErrorCodeException{
	private String action;
	
	public ActionException(ErrorCode errorCode) {
		this(null,errorCode);
	}
	
	public ActionException(String action, ErrorCode errorCode) {
		super(errorCode);
		this.action = action;
	}
	
	public ActionException(ErrorCode errorCode, Throwable cause) {
		super(errorCode, cause);
	}

	public ActionException(String action,ErrorCode errorCode, Throwable cause) {
		super(errorCode, cause);
		this.action = action;
	}
	
	public String getAction() {
		return action;
	}

	public String toString() {
		if(action == null) {
			return String.format("execute [%s] occer error,errorCode:[%s],errorDetails:%s",action,getErrorCode(),getMessage());
		}else {
			return String.format("errorCode:[%s],errorDetails:%s",getErrorCode(),getMessage());
		}
	}
	
}
