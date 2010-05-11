package com.company.project.facade.exception;

import cn.org.rapid_framework.util.ProfileUtils;

public class ProjectException extends RuntimeException{

	public ProjectException() {
		super();
	}

	public ProjectException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProjectException(String message) {
		super(message);
	}

	public ProjectException(Throwable cause) {
		super(cause);
	}
	
	
	public static void main(String[] args) {
		ProfileUtils.printCostTime("loop exception",new Runnable() {
			public void run() {
				for(int i = 0; i < 1000000; i++) {
					try {
						throw new ProjectException();
					}catch(Exception e) {
						//ignore
					}
				}				
			}
		});

	}

}
