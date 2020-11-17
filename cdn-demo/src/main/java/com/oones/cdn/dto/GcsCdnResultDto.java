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
public class GcsCdnResultDto extends CdnResultDto {
	Blob blob;

	public GcsCdnResultDto(String uri) {
		super(uri);
	}

	public GcsCdnResultDto(String uri, Blob blob) {
		super(uri);
		this.blob = blob;
	}

	public Blob getBlob() {
		return blob;
	}

	public void setBlob(Blob blob) {
		this.blob = blob;
	}

}
