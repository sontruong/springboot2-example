package com.example.demo.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(content = Include.NON_NULL)
public class RestMessage {
	private String code;

	private String message;

	private String error;

	private String error_description;
	
	@JsonInclude(value = Include.NON_EMPTY)
	private List<ObjectError> violations = new ArrayList<ObjectError>();

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getError_description() {
		return error_description;
	}

	public void setError_description(String error_description) {
		this.error_description = error_description;
	}

	public RestMessage() {
		this(null, null);
	}

	public RestMessage(String code) {
		this(code, null);
	}

	public RestMessage(String code, String message) {
		super();
		this.setCode(code);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<ObjectError> getViolations() {
		return violations;
	}

	public void setViolations(List<ObjectError> violations) {
		this.violations = violations;
	}
}
