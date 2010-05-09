package cn.org.rapid_framework.exception;

/**
 * 
 * @author badqiu
 *
 */
public class ActionException extends ErrorCodeException{
	//问题: 系统不能启动
	//错误提示: error_code:9959,error_detail:非法参数
	//操作(action): 验证身份, can null or lookupByErrorCode
	//原因(reason): 表单验证错误,可以lookupByErrorCodeAndAction
	//解决办法(fix): 请验证输入数据,可以lookupByErrorCodeAndAction
	private String action;
	
	public ActionException(ErrorCode errorEnum) {
		this(null,errorEnum);
	}
	
	public ActionException(String action, ErrorCode errorEnum) {
		super(errorEnum);
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

	public void setAction(String action) {
		this.action = action;
	}

	public String toString() {
		if(action == null) {
			return String.format("execute [%s] occer error,errorCode:[%s],errorDetails:%s",action,getErrorCode().getErrorCode(),getErrorCode().getErrorDetails());
		}else {
			return String.format("errorCode:[%s],errorDetails:%s",getErrorCode().getErrorCode(),getErrorCode().getErrorDetails());
		}
	}
	
}
