/**
 * 
 */
package com.oones.cdn.dto;

/**
 * @author son.truong
 *
 *         Nov 9, 2020
 */
public class CdnResultDto<T> {
	String uri;
	T t;

	public CdnResultDto(String uri) {
		super();
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}

}
