package cn.org.rapid_framework.exception;

/**
 * 用于异常标准化处理
 * 
 * @see ErrorCode
 * 
 * @author badqiu
 */
public class ErrorCodeException extends RuntimeException{
	//TODO 考虑是否需要suberror, 如parernt error=1000,child error=500, full_error=1000.500
	private ErrorCode errorCode;
	
	public ErrorCodeException(ErrorCode errorCode) {
		super();
		this.errorCode = errorCode;
	}
	
	public ErrorCodeException(ErrorCode errorCode,Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
	}
	
	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public String toString() {
		return String.format("errorCode:[%s],errorDetails:%s",errorCode.getErrorCode(),errorCode.getErrorDetails());
	}
	
}
