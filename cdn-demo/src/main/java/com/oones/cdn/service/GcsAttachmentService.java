package com.oones.cdn.service;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.StorageClass;
import com.oones.cdn.dto.GcsUploadResultDto;
import com.oones.cdn.exception.NotFoundException;
import com.oones.cdn.type.CdnAccessType;

public interface GcsAttachmentService {
	
	GcsUploadResultDto addFile(MultipartFile file, String uploadPath, String username, CdnAccessType type, Acl... acls) throws IOException;
	GcsUploadResultDto addFile(File file, String uploadPath, String username, CdnAccessType type, Acl... acls) throws IOException;
	void addFolder(File folder, String uploadPath, String username, Map<Integer, String> storeMap, CdnAccessType type, Acl... acls) throws IOException;
	Map<String, Blob> addFiles(String uploadPath, String username, Set<String> storeMap, CdnAccessType type, Acl... acls) throws IOException;
	
	Blob updatePermission(String src, CdnAccessType type, Acl acl) throws IOException;
	Blob copyFile(String src, String des) throws IOException;
	
	byte[] downloadFile(String fileIdentifier) throws IOException, NotFoundException;
	GcsUploadResultDto getFile(String fileIdentifier) throws IOException, NotFoundException;
	
	void deleteFile(String fileIdentifier) throws IOException;

	Collection<String> getFileList(String prefix) throws IOException;
	
	void updateRetentionPolicy(String bucketName, Long timeToLive) throws IOException;
	void deleteBucket(String bucketName) throws IOException;
	void createBucket(String bucketName, String location, StorageClass storageClass);
}
