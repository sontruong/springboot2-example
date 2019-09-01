package com.example.demo.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;

import javax.transaction.Transactional;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.demo.DemoApplicationTests;
import com.example.demo.domain.GPX;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Transactional
public class GPSControllerTest extends DemoApplicationTests {
	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	private MockHttpServletRequestBuilder buildUploadRequest() throws IOException {
		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/sample/sample.gpx"));
		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "sample", "text/plain",
				xml.getBytes());
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.fileUpload("/api/uploadtracker")
				.file(mockMultipartFile).accept(MediaType.APPLICATION_JSON);
		return builder;
	}
	
	@Test
	public void testNullUpload() throws Exception {
		mockMvc.perform(post("/api/uploadtracker").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testWrongFileUpload() throws Exception {
		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test", "text/plain",
				"test data".getBytes());
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.fileUpload("/api/uploadtracker")
				.file(mockMultipartFile).accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(builder).andExpect(status().isBadRequest());
	}

	@Test
	@Rollback(true)
	public void tesFileUpload() throws Exception {
		MockHttpServletRequestBuilder builder = buildUploadRequest();
		mockMvc.perform(builder).andExpect(status().isOk());
	}

	@Test
	@Rollback(true)
	public void testGetLastest() throws Exception {
		// check empty result
		MvcResult result = mockMvc.perform(get("/api/lastesttrack").accept(MediaType.APPLICATION_JSON)).andReturn();
		MockHttpServletResponse response = result.getResponse();
		Assert.assertEquals(response.getContentAsString(), "[]");
		
		
		// check one result
		MockHttpServletRequestBuilder builder = buildUploadRequest();
		mockMvc.perform(builder).andExpect(status().isOk());
		
		result = mockMvc.perform(get("/api/lastesttrack").accept(MediaType.APPLICATION_JSON)).andReturn();
		response = result.getResponse();
		Gson gson = new Gson();
		Type token = new TypeToken<Collection<GPX>>() {}.getType();
		Collection<GPX> gpxs = gson.fromJson(response.getContentAsString(), token);
		Assert.assertEquals(gpxs.size(), 1);
	}
	
	@Test
	@Rollback(true)
	public void testGetDetail() throws Exception {
		// check empty result
		mockMvc.perform(get("/api/trackdetail/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
				
		// check one result
		MockHttpServletRequestBuilder builder = buildUploadRequest();
		mockMvc.perform(builder).andExpect(status().isOk());
		
		MvcResult result = mockMvc.perform(get("/api/lastesttrack").accept(MediaType.APPLICATION_JSON)).andReturn();
		MockHttpServletResponse response = result.getResponse();
		Gson gson = new Gson();
		Type token = new TypeToken<Collection<GPX>>() {}.getType();
		Collection<GPX> gpxs = gson.fromJson(response.getContentAsString(), token);
		Assert.assertEquals(gpxs.size(), 1);
		
		GPX gpx = gpxs.iterator().next();
		mockMvc.perform(get("/api/trackdetail/" + gpx.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}
}
