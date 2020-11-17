package com.oones.cdn.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.api.gax.paging.Page;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.BlobInfo.Builder;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.CopyWriter;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.BlobListOption;
import com.google.cloud.storage.Storage.BlobTargetOption;
import com.google.cloud.storage.Storage.PredefinedAcl;
import com.google.cloud.storage.StorageClass;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;
import com.oones.cdn.dto.GcsCdnResultDto;
import com.oones.cdn.exception.ExistException;
import com.oones.cdn.exception.NotFoundException;
import com.oones.cdn.type.CdnAccessType;
import com.oones.cdn.utils.DateUtils;

@Service
public class GcsAttachmentServiceImpl implements CdnService {

	private static final Logger LOG = LoggerFactory.getLogger(GcsAttachmentServiceImpl.class);
	public static final String ATTR_IDENTIFIER = "identifier";
	public static final String ATTR_FILENAME = "user.filename";
	public static final String ATTR_UPDATED_BY = "user.updatedBy";
	public static final String ATTR_MODIFIED_ON = "user.modifiedOn";

	@Value("${cdn.gcs.serviceAccountEmail}")
	private String serviceAccountEmail;

	@Value("${cdn.gcs.serviceAccountPrivateKey}")
	private String serviceAccountPrivateFileKey;

	@Value("${cdn.gcs.bucket}")
	private String bucketName;

	@Value("${cdn.gcs.appName}")
	private String appName;
	
	@Value("${cdn.gcs.appId}")
	private String projectId;

	@Value("${cdn.gcs.rootPath}")
	private String rootPath;

	@Autowired
	ResourcePatternResolver resourcePatternResolver;

	private Storage storageClient;

	@Override
	public GcsCdnResultDto addFile(MultipartFile file, String uploadPath, String username, CdnAccessType type, Acl... acls) throws IOException {
		LOG.info("Uploading new file {} for entity {}, name {}", file, uploadPath);
		String identifier = UUID.randomUUID().toString();
		GcsCdnResultDto blob = insertIntoBucket(file.getInputStream(), file.getOriginalFilename(), uploadPath, identifier, username, type, acls);
		return blob;
	}
	
	@Override
	public GcsCdnResultDto addFile(File file, String uploadPath, String username, CdnAccessType type, Acl... acls) throws IOException {
		LOG.info("Uploading new file {} for entity {}, name {}", file, uploadPath);
		String identifier = UUID.randomUUID().toString();
		System.out.println("identifier: " + identifier);
		GcsCdnResultDto blob = insertIntoBucket(new FileInputStream(file), file.getName(), uploadPath, identifier, username, type, acls);
		return blob;
	}
	
	@Override
	public void addFolder(File folder, String uploadPath, String username, Map<Integer, String> storeMap, CdnAccessType type, Acl... acls) throws IOException {
		LOG.info("Uploading new folder {} for entity {}, name {}", folder, uploadPath);
		if (!folder.exists() || !folder.isDirectory()) {
			LOG.error("Folder '{}' does not exist ", folder);
			return;
		}
		File[] files = folder.listFiles();
		if (null == files || 0 == files.length) {
			LOG.error("Folder '{}' does not have any file ", folder);
			return;
		}
		if (null == storeMap) {
			storeMap = new HashMap<>();
		}
		for (File file: files) {
			if (file.isDirectory()) {
				addFolder(file, uploadPath + "/" + file.getName(), username, storeMap, type, acls);
				continue;
			}
			LOG.info("Uploading new file {} for entity {}, name {}", file, uploadPath);
			String identifier = UUID.randomUUID().toString();
			System.out.println("identifier: " + identifier);
			GcsCdnResultDto resultDto = insertIntoBucket(new FileInputStream(file), file.getName(), uploadPath, identifier, username, type, acls);
			Blob blob = resultDto.getBlob();
			storeMap.put(file.getAbsolutePath().hashCode(), blob.getSelfLink());
		}
	}
	
	@Override
	public Map<String, Blob> addFiles(String uploadPath, String username, Set<String> filePaths, CdnAccessType type, Acl... acls) throws IOException {
		if (CollectionUtils.isEmpty(filePaths)) {
			LOG.error("There are no file in list");
			return new HashMap<>();
		}
		Map<String, Blob> result = new HashMap<>();
		for (String filePath: filePaths) {
			File file = new File(filePath);
			if (!file.exists() || file.isDirectory()) {
				continue;
			}
			LOG.info("Uploading new file {} for entity {}, name {}", file, uploadPath);
			String identifier = UUID.randomUUID().toString();
			String uploadName = filePath.substring(filePath.indexOf("/" + uploadPath + "/") + 1);
			GcsCdnResultDto resultDto = insertIntoBucket(new FileInputStream(file), uploadName, "", identifier, username, type, acls);
			Blob blob = resultDto.getBlob();
			result.put(filePath, blob);
		}
		return result;
	}

	@Override
	public Blob copyFile(String src, String des) throws IOException {
		initClient();
		Blob blob = storageClient.get(bucketName, src);
		if (!blob.exists()) {
			throw new NotFoundException("NOT_FOUND");
		}
		BlobId blobId = BlobId.of(bucketName, des);
		CopyWriter copy = blob.copyTo(blobId);
		return copy.getResult();
	}
	
	@Override
	public Blob updatePermission(String src, CdnAccessType type, Acl acl) throws IOException {
		initClient();
		Blob blob = storageClient.get(bucketName, src);
		if (!blob.exists()) {
			throw new NotFoundException("NOT_FOUND");
		}
		if (null != acl) {
			storageClient.updateAcl(blob.getBlobId(), acl);
		}
		BlobTargetOption target = CdnAccessType.toBlobTargetOption(type);
		blob = storageClient.update(blob, target);
		return blob;
	}
	
	@Override
	public byte[] downloadFile(String fileIdentifier) throws IOException, NotFoundException {
		try {
			LOG.info("Downloading fileIdentifier {}", fileIdentifier);
			initClient();
			Blob blob = storageClient.get(bucketName, fileIdentifier);
			byte[] inputStream = blob.getContent();
			return inputStream;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new NotFoundException("NOT_FOUND");
		}
	}
	
	@Override
	public GcsCdnResultDto getFile(String fileIdentifier) throws IOException, NotFoundException {
		try {
			LOG.info("Getting fileIdentifier {}", fileIdentifier);
			initClient();
			Blob blob = storageClient.get(bucketName, fileIdentifier);
			GcsCdnResultDto result = new GcsCdnResultDto("");
			result.setBlob(blob);
			return result;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new NotFoundException("NOT_FOUND");
		}
	}

	@Override
	public void deleteFile(String fileIdentifier) throws IOException {
		try {
			LOG.info("Deleting file for fileIdentifier {}", fileIdentifier);
			initClient();
			Blob blob = storageClient.get(bucketName, fileIdentifier);
			storageClient.delete(blob.getBlobId());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new NotFoundException("NOT_FOUND");
		}
	}

	@Override
	public Collection<String> getFileList(String prefix) throws IOException {
		LOG.info("Listing files for prefix {}", prefix);
		initClient();
		Bucket bucket = storageClient.get(bucketName);
		BlobListOption listOption = BlobListOption.fields();
		if (!StringUtils.isEmpty(prefix)) {
			listOption = BlobListOption.prefix(prefix);
		}
		
		Page<Blob> page = bucket.list(listOption);
		Iterable<Blob> items = page.iterateAll();
		if (!items.iterator().hasNext()) {
			return new ArrayList<>();
		}
		Collection<String> results = new ArrayList<>();
		
		items.forEach(blob -> {
			results.add(blob.getName());
		});
		
		return results;
	}

	/*
	 * private long getModifiedOn(AttachmentDTO fileAttributes) { return
	 * fileAttributes.getUploadDate() != null ?
	 * fileAttributes.getUploadDate().toInstant(ZoneOffset.UTC).toEpochMilli() :
	 * System.currentTimeMillis(); }
	 */

	private String buildUri(String... paths) {
		UriComponents builder = UriComponentsBuilder.fromPath(rootPath).pathSegment(paths).build();
		return builder.toUriString().replaceFirst("^/", "");
	}

	private GcsCdnResultDto insertIntoBucket(InputStream file, String fileName, String uploadPath, String identifier, String username, CdnAccessType type, Acl... acls)
			throws IOException {

		initClient();
		Map<String, String> metadata = new HashMap<>();
		metadata.put(ATTR_UPDATED_BY, username);
		metadata.put(ATTR_FILENAME, fileName);
		metadata.put(ATTR_IDENTIFIER, identifier);
		metadata.put(ATTR_MODIFIED_ON, DateUtils.getDateFormat(new Date(), "yyyy-MM-dd hh:mm:ss"));
		
		String uri = buildUri(uploadPath, identifier);
		GcsCdnResultDto result = new GcsCdnResultDto(uri);
	    BlobId blobId = BlobId.of(bucketName, uri);
	    
	    Builder builder = BlobInfo.newBuilder(blobId).setMetadata(metadata);
		if (null != acls && 0 < acls.length) {
			builder.setAcl(new ArrayList<>(Arrays.asList(acls)));
		}
		BlobInfo blobInfo = builder.build();
	    byte[] content = IOUtils.toByteArray(file);
	    Blob blob = null;
	    if (type == CdnAccessType.PUBLIC_READ) {
	    	BlobTargetOption target = BlobTargetOption.predefinedAcl(PredefinedAcl.PUBLIC_READ);
	    	blob = storageClient.create(blobInfo, content, target);
	    } else {
	    	blob = storageClient.create(blobInfo, content);
	    }
	    result.setBlob(blob);
	    return result;
	}

	private void initClient() {
		try {
			if (null == storageClient) {
				storageClient = StorageOptions.newBuilder().setProjectId(projectId).setCredentials(authorize()).build().getService();
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	private Credentials authorize()
			throws GeneralSecurityException, IOException, IllegalArgumentException {
		Resource resource = new ClassPathResource(serviceAccountPrivateFileKey);
		Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(resource.getFile()));
		return credentials;
	}
	
	public void createBucket(String bucketName, String location, StorageClass storageClass) {
		initClient();
		try {
			storageClient
					.create(BucketInfo.newBuilder(bucketName).setStorageClass(storageClass).setLocation(location).build());
		} catch (StorageException e) {
			if (e.getCode() == HttpStatus.CONFLICT.value()) {
				throw new ExistException(e.getMessage());
			} else {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void updateRetentionPolicy(String bucketName, Long timeToLive) throws IOException {
		initClient();
		storageClient.update(BucketInfo.newBuilder(bucketName).setRetentionPeriod(timeToLive).build());
	}
	
	@Override
	public void deleteBucket(String bucketName) throws IOException {
		initClient();
		Bucket bucket = storageClient.get(bucketName);
		bucket.delete();
	}
}
