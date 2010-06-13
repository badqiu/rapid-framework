package cn.org.rapid_framework.util;

import java.util.ArrayList;
import java.util.List;

public class OperationResult {
	
	private Exception exception;
	private Object result;
	private String operatipn;
	private String detailMessage;
	
	//考虑是否需要isSuccess手工设置,
	//删除理由:可以根据exception,errorEnum计算出来
	//存在理由:1.表单数据验证错误,可以手工设置为false
		//删除理由:表单验证错误,一定需要设置errorEnum
//	private boolean isSuccess = true; 
	
	//待考虑是否setErrorEnum时同时设置isSuccess为false
	//考虑是否应该是 errors
	private Object errorEnum = null; 
	
	//是否考虑增加子操作结果
	private List<OperationResult> childs = new ArrayList();
	
//	public void setSuccess(boolean isSuccess) {
//		this.isSuccess = isSuccess;
//	}

	public OperationResult() {
	}
	
	public OperationResult(String operatipn) {
		super();
		this.operatipn = operatipn;
	}

	public boolean isSuccess() {
		return !hasError();
	}

	public boolean hasError() {
		if(exception != null || errorEnum != null) {
			return true;
		}
		return false;
	}
	
	public String getOperatipn() {
		return operatipn;
	}
	public void setOperatipn(String operatipn) {
		this.operatipn = operatipn;
	}
	public String getDetailMessage() {
		return detailMessage;
	}
	public void setDetailMessage(String detailMessage) {
		this.detailMessage = detailMessage;
	}
	public Exception getException() {
		return exception;
	}
	public void setException(Exception exception) {
		this.exception = exception;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}

	public Object getErrorEnum() {
		return errorEnum;
	}

	public void setErrorEnum(Object errorEnum) {
		this.errorEnum = errorEnum;
	}
	
}
