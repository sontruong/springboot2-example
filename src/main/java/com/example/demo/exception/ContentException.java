package com.example.demo.exception;

public class ContentException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id = "";

	public ContentException(String msg) {
		super(msg);
	}

	public ContentException(String id, String msg) {
		super(msg);
		this.id = id;
	}

	public String getId() {
		return id;
	}
}