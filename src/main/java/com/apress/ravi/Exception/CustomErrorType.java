package com.apress.ravi.Exception;

import com.apress.ravi.dto.UsersDTO;

public class CustomErrorType extends UsersDTO{

	private String errorMessage;
	
	public CustomErrorType(final String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
}
