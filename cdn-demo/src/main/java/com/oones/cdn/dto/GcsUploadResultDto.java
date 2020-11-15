/**
 * 
 */
package com.oones.cdn.dto;

import com.google.cloud.storage.Blob;

/**
 * @author son.truong
 *
 *         Nov 9, 2020
 */
public class GcsUploadResultDto {
	String uri;
	Blob blob;

	public GcsUploadResultDto(String uri) {
		super();
		this.uri = uri;
	}

	public GcsUploadResultDto(String uri, Blob blob) {
		super();
		this.uri = uri;
		this.blob = blob;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Blob getBlob() {
		return blob;
	}

	public void setBlob(Blob blob) {
		this.blob = blob;
	}

}
