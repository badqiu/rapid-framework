package cn.org.rapid_framework.exception;

/**
 * @author badqiu
 * @see ErrorCode
 * @see ErrorCodeException
 */
public class ErrorCodeImpl implements ErrorCode{
	private static final long serialVersionUID = 6335800965783920460L;
	
	private String errorCode;
	private String message;
	
	public ErrorCodeImpl(String errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}
	
	public String getErrorCode() {
		return errorCode;
	}

	public String getMessage() {
		return message;
	}
	
}
