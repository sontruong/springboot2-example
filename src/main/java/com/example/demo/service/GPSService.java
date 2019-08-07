package com.example.demo.service;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import com.example.demo.domain.GPX;

public interface GPSService {

	public void importGPSTracker(MultipartFile multipartFile)
			throws IOException, ParserConfigurationException, SAXException;

	public Collection<GPX> getLastest();

	public GPX getDetail(Long id);

	public void download(Long id, HttpServletResponse response) throws IOException;
}
