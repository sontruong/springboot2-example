package com.oones.cdn.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(content = Include.NON_EMPTY)
public class TextDTO {
	private String txt;
	private String txt2;
	public String getTxt() {
		return txt;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}

	public String getTxt2() {
		return txt2;
	}

	public void setTxt2(String txt2) {
		this.txt2 = txt2;
	}
	
}
