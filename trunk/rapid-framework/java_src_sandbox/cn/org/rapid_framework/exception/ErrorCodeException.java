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
		super();
		this.errorCode = errorCode;
	}

	public ErrorCodeException(ErrorCode errorCode,String message) {
		super(message);
		this.errorCode = errorCode;
	}
	
	public ErrorCodeException(ErrorCode errorCode,Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
	}
	
	public ErrorCodeException(ErrorCode errorCode,String message,Throwable cause) {
		super(message,cause);
		this.errorCode = errorCode;
	}
	
	/**
	 * 得到errorCode,有可能返回null
	 * @return
	 */
	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public String toString() {
		if(errorCode == null) return super.toString();
		return String.format("errorCode:[%s],errorCodeDetails:%s,exception message:%s",errorCode.getErrorCode(),errorCode.getErrorCodeDetails(),super.toString());
	}
	
}
