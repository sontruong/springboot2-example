package com.example.demo.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.xml.sax.SAXParseException;

import com.example.demo.dto.RestMessage;
import com.example.demo.exception.ContentException;
import com.example.demo.exception.NotfoundException;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {
	
	@ExceptionHandler(value = { ContentException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public RestMessage handle(ContentException ex) {
		return new RestMessage("400", ex.getMessage());
	}
	
	@ExceptionHandler(value = { SAXParseException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public RestMessage handle(SAXParseException ex) {
		return new RestMessage("400", ex.getMessage());
	}
	
	@ExceptionHandler(value = { NotfoundException.class })
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public RestMessage handle(NotfoundException ex) {
		return new RestMessage("404", ex.getMessage());
	}
	
	
}
