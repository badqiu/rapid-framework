package cn.org.rapid_framework.exception;

/**
 * @author badqiu
 * @see ErrorCode
 * @see ErrorCodeException
 */
public class ErrorCodeImpl implements ErrorCode{
	private String errorCode;
	private String errorDetails;
	
	public ErrorCodeImpl(String errorCode, String errorDetails) {
		this.errorCode = errorCode;
		this.errorDetails = errorDetails;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorDetails() {
		return errorDetails;
	}
	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}
	
}
