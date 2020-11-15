/**
 * 
 */
package com.oones.cdn.exception;

/**
 * @author son.truong
 * Apr 13, 2017 11:03:56 PM
 */
public class NotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id = "";

	public NotFoundException(String msg) {
		super(msg);
	}

	public NotFoundException(String id, String msg) {
		super(msg);
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
