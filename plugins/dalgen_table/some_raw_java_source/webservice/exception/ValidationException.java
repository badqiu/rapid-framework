package com.company.project.facade.exception;

import org.springframework.validation.Errors;

public class ValidationException extends RuntimeException{
	private Errors errors;

	public ValidationException() {
		super();
	}

	public ValidationException(Errors errors) {
		super();
		this.errors = errors;
	}
	
	public ValidationException(String message, Errors errors) {
		super(message);
		this.errors = errors;
	}

	public ValidationException(String message) {
		super(message);
	}
	
	public Errors getErrors() {
		return errors;
	}

	public void setErrors(Errors errors) {
		this.errors = errors;
	}
}
