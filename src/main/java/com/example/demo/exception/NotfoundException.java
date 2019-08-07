package com.example.demo.exception;

public class NotfoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id = "";

	public NotfoundException(String msg) {
		super(msg);
	}

	public NotfoundException(String id, String msg) {
		super(msg);
		this.id = id;
	}

	public String getId() {
		return id;
	}
}