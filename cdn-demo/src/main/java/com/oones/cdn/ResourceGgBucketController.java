/**
 * 
 */
package com.oones.cdn;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.StorageClass;
import com.oones.cdn.dto.CdnResultDto;
import com.oones.cdn.dto.SuccessMessage;
import com.oones.cdn.dto.TextDTO;
import com.oones.cdn.service.CdnService;
import com.oones.cdn.service.GcsCdnServiceImpl;
import com.oones.cdn.type.CdnAccessType;
import com.oones.cdn.utils.FileUtils;

import io.swagger.annotations.Api;

/**
 * @author son.truong
 */
@Controller
@Api(value = "", tags = "Resource's controller")
@RequestMapping(value = "api")
public class ResourceGgBucketController {

	@Autowired
	CdnService gcsService;
	
    /** constructor of UserResourceVersionController*/
    public ResourceGgBucketController(){
        super();
    }
    
	@RequestMapping(value = "resource/gcs", method = RequestMethod.GET, produces = AppConfig.APPLICATION_CONSUMES)
	public @ResponseBody ResponseEntity<Collection<String>> getList(@RequestParam String prefix) throws Exception {
		return new ResponseEntity<Collection<String>>(gcsService.getFileList(prefix), HttpStatus.OK);
	}
	
	@RequestMapping(value = "resource/gcs", method = RequestMethod.POST, produces = AppConfig.APPLICATION_CONSUMES)
	public @ResponseBody ResponseEntity<TextDTO> add(@RequestParam("file") MultipartFile file) throws Exception {
		TextDTO dto = new TextDTO();
		dto.setTxt(gcsService.addFile(file, "test", "vinhson", CdnAccessType.PUBLIC_READ).getUri());
		return new ResponseEntity<TextDTO>(dto, HttpStatus.OK);
	}
	
	@RequestMapping(value = "resource/gcs/download", method = RequestMethod.POST, produces = AppConfig.APPLICATION_CONSUMES)
	public @ResponseBody void download(HttpServletResponse response, @RequestParam String src) throws Exception {
		try {
			CdnResultDto<Blob> file = gcsService.getFile(src);
			String fileName = file.getT().getMetadata().get(GcsCdnServiceImpl.ATTR_FILENAME);
			response.setHeader("Content-disposition", "attachment; filename=" + fileName);
			if (null != FileUtils.getMimeType(fileName)) {
				response.setContentType(FileUtils.getMimeType(fileName));
			} else {
				response.setContentType(AppConfig.APPLICATION_CONTENT_TYPES);
			}
			response.getOutputStream().write(file.getT().getContent());
			response.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "resource/gcs/delete", method = RequestMethod.DELETE, produces = AppConfig.APPLICATION_CONSUMES)
	public @ResponseBody ResponseEntity<SuccessMessage>  delete(HttpServletResponse response, @RequestParam String src) throws Exception {
		try {
			gcsService.deleteFile(src);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<SuccessMessage>(new SuccessMessage(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "resource/gcs/copy", method = RequestMethod.POST, produces = AppConfig.APPLICATION_CONSUMES)
	public @ResponseBody ResponseEntity<TextDTO> copy(@RequestHeader(required=false) String authorization, 
																		@RequestParam String src, @RequestParam String des) throws Exception {
		TextDTO dto = new TextDTO();
		dto.setTxt(gcsService.copyFile(src, des).getMediaLink());
		return new ResponseEntity<TextDTO>(dto, HttpStatus.OK);
	}
	
	@RequestMapping(value = "resource/gcs/update_permission", method = RequestMethod.PUT, produces = AppConfig.APPLICATION_CONSUMES)
	public @ResponseBody ResponseEntity<TextDTO> updatePermission(@RequestParam String src, 
			@RequestParam CdnAccessType accessType, @RequestParam(required = false) Acl acl) throws Exception {
		TextDTO dto = new TextDTO();
		dto.setTxt(gcsService.updatePermission(src, accessType, acl).getMediaLink());
		return new ResponseEntity<TextDTO>(dto, HttpStatus.OK);
	}
	
	@RequestMapping(value = "resource/gcs/bucket", method = RequestMethod.POST, produces = AppConfig.APPLICATION_CONSUMES)
	public @ResponseBody ResponseEntity<SuccessMessage> createBucket(HttpServletResponse response,
			@RequestParam String bucketName, @RequestParam String location, @RequestParam StorageClass storageClass) throws Exception {
		gcsService.createBucket(bucketName, location, storageClass);
		return new ResponseEntity<SuccessMessage>(new SuccessMessage(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "resource/gcs/bucket", method = RequestMethod.DELETE, produces = AppConfig.APPLICATION_CONSUMES)
	public @ResponseBody ResponseEntity<SuccessMessage> deleteBucket(HttpServletResponse response,
			@RequestParam String bucketName) throws Exception {
		gcsService.deleteBucket(bucketName);
		return new ResponseEntity<SuccessMessage>(new SuccessMessage(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "resource/gcs/bucket/ttl", method = RequestMethod.PUT, produces = AppConfig.APPLICATION_CONSUMES)
	public @ResponseBody ResponseEntity<SuccessMessage> setTTL(HttpServletResponse response, @RequestParam String bucketName, @RequestParam Long timeToLive) throws Exception {
		gcsService.updateRetentionPolicy(bucketName, timeToLive);
		return new ResponseEntity<SuccessMessage>(new SuccessMessage(), HttpStatus.OK);
	}
}
