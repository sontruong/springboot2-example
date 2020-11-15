/**
 * 
 */
package com.oones.cdn.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 */
@JsonInclude(value = Include.NON_NULL)
public class SuccessMessage {

	private String code;

	private String message;
	
	private Date startTime;
	private Date endTime;

	public SuccessMessage(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public SuccessMessage(String code) {
		super();
		this.code = code;
	}

	public SuccessMessage() {
		super();
		this.code = "SUCCESS";
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
