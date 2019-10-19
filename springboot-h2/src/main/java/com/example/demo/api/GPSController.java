/**
 * 
 */
package com.example.demo.api;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import com.example.demo.domain.GPX;
import com.example.demo.dto.RestMessage;
import com.example.demo.exception.ContentException;
import com.example.demo.service.GPSService;
import com.example.demo.utils.AppConfig;
import com.example.demo.utils.ApplicationMessage;

@Controller
@RequestMapping(value = "api")
public class GPSController {

	@Autowired
	GPSService gpsService;

	@RequestMapping(value = "uploadtracker", method = RequestMethod.POST, produces = AppConfig.APPLICATION_CONSUMES)
	public @ResponseBody ResponseEntity<RestMessage> upload(@RequestBody MultipartFile file) throws ContentException, IOException, ParserConfigurationException, SAXException {
		gpsService.importGPSTracker(file);
		return new ResponseEntity<RestMessage>(new RestMessage(ApplicationMessage.SUCCESS), HttpStatus.OK);
	}
	
	@RequestMapping(value = "lastesttrack", method = RequestMethod.GET, produces = AppConfig.APPLICATION_CONSUMES)
	public @ResponseBody ResponseEntity<Collection<GPX>> getLastest() throws ContentException, IOException, ParserConfigurationException, SAXException {
		Collection<GPX> result = gpsService.getLastest();
		return new ResponseEntity<Collection<GPX>>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "trackdetail/{id}", method = RequestMethod.GET, produces = AppConfig.APPLICATION_CONSUMES)
	public @ResponseBody ResponseEntity<GPX> getdetail(@PathVariable Long id) throws ContentException, IOException, ParserConfigurationException, SAXException {
		GPX result = gpsService.getDetail(id);
		return new ResponseEntity<GPX>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "trackdetail/download/{id}", method = RequestMethod.GET, produces = AppConfig.APPLICATION_CONSUMES)
	public void download(@PathVariable Long id, HttpServletResponse response) throws ContentException, IOException, ParserConfigurationException, SAXException {
		gpsService.download(id, response);
	}
}
