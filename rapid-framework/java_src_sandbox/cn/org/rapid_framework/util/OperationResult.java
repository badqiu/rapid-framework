package cn.org.rapid_framework.util;

import java.util.ArrayList;
import java.util.List;

public class OperationResult {
	List<Enum> errors = new ArrayList();
	public boolean isSuccess() {
		return errors == null || errors.isEmpty();
	}
	public boolean isError() {
		return !isSuccess();
	}
	public OperationResult addError(Enum str) {
		errors.add(str);
		return this;
	}
	public List<Enum> getErrors() {
		return errors;
	}
	public void setErrors(List<Enum> errors) {
		this.errors = errors;
	}
	
	
}
