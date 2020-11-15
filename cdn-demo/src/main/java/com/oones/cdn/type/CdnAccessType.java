/**
 * 
 */
package com.oones.cdn.type;

import com.google.cloud.storage.Storage.BlobTargetOption;
import com.google.cloud.storage.Storage.PredefinedAcl;

/**
 * @author son.truong
 *
 * Nov 4, 2020
 */
public enum CdnAccessType {
	PRIVATE,
	PUBLIC_READ;
	
	public static BlobTargetOption toBlobTargetOption(CdnAccessType type) {
		BlobTargetOption target = BlobTargetOption.predefinedAcl(PredefinedAcl.PRIVATE);
		switch (type) {
		case PUBLIC_READ:
			target = BlobTargetOption.predefinedAcl(PredefinedAcl.PUBLIC_READ);
			break;
		default:
			break;
		}
		return target;
	}
}
