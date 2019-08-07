package com.example.demo.utils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class AppUtils {

	public static Element readXML(File file) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(file);
		return document.getDocumentElement();
	}
	
	public static Node getNode(Element element, String tagName) {
		if (null == element || null == tagName) {
			return null;
		}
		NodeList list = element.getElementsByTagName(tagName);
		if (list.getLength() > 0) {
			return list.item(0);
		}
		return null;
	}
	
	public static NodeList getNodes(Element element, String tagName) {
		if (null == element || null == tagName) {
			return null;
		}
		return element.getElementsByTagName(tagName);
	}
	
	public static String getNodeValue(Node node) {
		if (null == node) {
			return null;
		}
		return node.getTextContent();
	}
	
	public static String getNodeAttr(Node node, String attrName) {
		if (null == node) {
			return null;
		}
		NamedNodeMap map = node.getAttributes();
		for (int i = 0; i < map.getLength(); i++) {
			if (map.item(i).getNodeName().equalsIgnoreCase(attrName)) {
				return map.item(i).getTextContent();
			}
		}
		return node.getTextContent();
	}
	
	public static Node getChildNode(Node node, String childName) {
		if (null == node) {
			return null;
		}
		
		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).getNodeName().equalsIgnoreCase(childName)) {
				return list.item(i);
			}
		}
		return null;
	}
	
	public static String getChildNodeValue(Node node, String childName) {
		if (null == node) {
			return null;
		}
		
		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).getNodeName().equalsIgnoreCase(childName)) {
				return list.item(i).getTextContent();
			}
		}
		return null;
	}

	public static NodeList getNodes(Node node, String tagName) {
		Element eElement = (Element) node;
		return eElement.getElementsByTagName(tagName);
	}
	
	public static Date parseDate(String date) throws ParseException {
		if (null == date || date.equalsIgnoreCase("")) {
			return null;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
		ZonedDateTime result = ZonedDateTime.parse(date, formatter);
		return Date.from(result.toInstant());
	}
	
	public static Sort createSort(Direction direction, String property) {
		if (null != property && !property.equalsIgnoreCase("")) {
			if (null == direction) {
				direction = Direction.ASC;
			}
			Sort sort = new Sort(direction, property);
			return sort;
		}
		return null;
	}
	
	public static PageRequest createPageRequest(Integer page, Integer limit, Sort sort) {

		if (null == page || 0 > page) {
			page = 0;
		}
		if (null == limit || 1 > limit) {
			limit = 10;
		}

		if (null == sort) {
			return new PageRequest(page, limit);
		} 
		
		return new PageRequest(page, limit, sort);
	}
}
