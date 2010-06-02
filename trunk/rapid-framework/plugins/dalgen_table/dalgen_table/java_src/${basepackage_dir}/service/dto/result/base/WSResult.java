package com.company.project.service.dto.result.base;


public class WSResult implements java.io.Serializable{
	private static final long serialVersionUID = -5186856885619322687L;
	public final static String SYSTEM_UNKNOW_ERROR = "SYSTEM_UNKNOW_ERROR";
	
    private boolean isSuccess = true;
	private String errorCode;
	private String errorDetails;
	
	public WSResult(){
	}
	
	   
    public WSResult(String errorCode, String errorDetails) {
        super();
        this.isSuccess = false;
        this.errorCode = errorCode;
        this.errorDetails = errorDetails;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
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
