package cn.org.rapid_framework.exception;

/**
 * 用于异常标准化处理
 * 
 * @see ErrorCode
 * 
 * @author badqiu
 */
public class ErrorCodeException extends RuntimeException implements ErrorCode{
	//TODO 考虑是否需要suberror, 如parernt error=1000,child error=500, full_error=1000.500
	private String errorCode;
	private ErrorCode errorCodeObject; //FIXME errorCodeObject现在没有用处
	
	public ErrorCodeException() {
		super();
	}

	public ErrorCodeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ErrorCodeException(String message) {
		super(message);
	}

	public ErrorCodeException(Throwable cause) {
		super(cause);
	}

	public ErrorCodeException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode.getErrorCode();
		this.errorCodeObject = errorCode;
	}

	public ErrorCodeException(ErrorCode errorCode,String message) {
		super(message);
		this.errorCode = errorCode.getErrorCode();
		this.errorCodeObject = errorCode;
	}
	
	public ErrorCodeException(ErrorCode errorCode,Throwable cause) {
		super(errorCode.getMessage(),cause);
		this.errorCode = errorCode.getErrorCode();
		this.errorCodeObject = errorCode;
	}
	
	public ErrorCodeException(ErrorCode errorCode,String message,Throwable cause) {
		super(message,cause);
		this.errorCode = errorCode.getErrorCode();
		this.errorCodeObject = errorCode;
	}
	
	/**
	 * 得到errorCode,有可能返回null
	 * @return
	 */
	public String getErrorCode() {
		return errorCode;
	}

	public String toString() {
		if(errorCode == null) return super.toString();
		return String.format("errorCode:[%s],message:[%s]",errorCode,getMessage());
	}
	
}
