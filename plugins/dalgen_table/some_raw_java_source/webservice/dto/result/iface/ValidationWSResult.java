package com.company.project.facade.dto.result.iface;

import org.springframework.validation.Errors;

public interface ValidationWSResult extends WSResult {

	public Errors getValidateErrors();
	
	public void setValidateErrors(Errors errors);
	
}
