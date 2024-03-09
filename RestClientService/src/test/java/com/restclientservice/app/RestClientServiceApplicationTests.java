package com.restclientservice.app;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restclientservice.app.service.RestService;
import com.restclientservice.app.service.dto.Employee;

@SpringBootTest
@AutoConfigureMockRestServiceServer
@ExtendWith(MockitoExtension.class)
class RestClientServiceApplicationTests {

	@Autowired
	@InjectMocks
	private RestService rest;
	@SpyBean
	RestClient.Builder builder;

	private String uri;
	private Map<String, String> headers;

	private MockRestServiceServer mockServer;
	
	@Value("classpath:employee.json")
	private Resource resource;
	
	private Employee emp;
	private ObjectMapper mapper;

	private String json;
	
	@BeforeEach
	void setup() throws URISyntaxException, JsonProcessingException {
		uri = "http://some-dummy-uri/data";
		headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("apiKey", "12345");
		
		emp = new Employee(23,"John Doe",30000);
		mapper = new ObjectMapper();
		
		json = mapper.writeValueAsString(emp);
	}

	@Test
	void GetRequestTest() throws URISyntaxException {

		mockServer = MockRestServiceServer.bindTo(builder).build();
		mockServer.expect(requestTo((uri))).andExpect(method(HttpMethod.GET))
				.andExpect(header("Content-Type", "application/json")).andExpect(header("apiKey", "12345"))
				.andRespond(withSuccess("This is dummy data", MediaType.APPLICATION_JSON));

		ResponseEntity<String> respEntity = rest.getRequest();
		mockServer.verify();
		assertEquals("This is dummy data", respEntity.getBody());
	}

	@Test
	void PostRequestTest() throws URISyntaxException {

		mockServer = MockRestServiceServer.bindTo(builder).build();
		mockServer.expect(requestTo(uri)).andExpect(method(HttpMethod.POST))
		.andExpect(content().json(json))
				.andExpect(header("Content-Type", "application/json")).andExpect(header("apiKey", "12345"))
				.andRespond(withSuccess());

		ResponseEntity<Void> respEntity = rest.postRequest(emp);
		mockServer.verify();
		assertEquals(HttpStatus.OK.value(), respEntity.getStatusCode().value());
	}
	
	@Test
	void GetRequestJSONResponseTest() throws URISyntaxException {

		mockServer = MockRestServiceServer.bindTo(builder).build();
		mockServer.expect(requestTo(uri)).andExpect(method(HttpMethod.GET))
				.andExpect(header("Content-Type", "application/json")).andExpect(header("apiKey", "12345"))
				.andRespond(withSuccess(resource,MediaType.APPLICATION_JSON));

		ResponseEntity<Employee> respEntity = rest.getRequestExpectingJsonResponse();
		mockServer.verify();
		assertEquals(HttpStatus.OK.value(), respEntity.getStatusCode().value());
	}
}
