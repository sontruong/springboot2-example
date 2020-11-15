/**
 * 
 */
package com.oones.cdn.exception;

/**
 * @author son.truong
 * Nov 14, 2020 11:03:56 PM
 */
public class ExistException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id = "";

	public ExistException(String msg) {
		super(msg);
	}

	public ExistException(String id, String msg) {
		super(msg);
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
