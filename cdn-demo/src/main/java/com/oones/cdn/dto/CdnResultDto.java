/**
 * 
 */
package com.oones.cdn.dto;

/**
 * @author son.truong
 *
 *         Nov 9, 2020
 */
public abstract class CdnResultDto {
	String uri;

	public CdnResultDto(String uri) {
		super();
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
}
