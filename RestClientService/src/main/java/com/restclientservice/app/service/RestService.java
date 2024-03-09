package com.restclientservice.app.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import com.restclientservice.app.service.dto.Employee;

@Service
public class RestService {

	@Autowired
	RestClient.Builder builder;
	
	public ResponseEntity<String> getRequest() throws URISyntaxException {
		URI uri = new URI("http://some-dummy-uri/data");
		Map<String,String> headers = new HashMap<String, String>();
		headers.put("Content-Type","application/json");
		headers.put("apiKey","12345");	
		
		RestClient client = builder.build();
		return client.get().uri(uri)
				.headers((httpHeader) -> headers.keySet().forEach(key -> httpHeader.add(key, headers.get(key))))
				.retrieve().toEntity(String.class);
	}
	
	public ResponseEntity<Void> postRequest(Employee body) throws URISyntaxException {
		URI uri = new URI("http://some-dummy-uri/data");
		Map<String,String> headers = new HashMap<String, String>();
		headers.put("Content-Type","application/json");
		headers.put("apiKey","12345");	
		
		RestClient client = builder.build();
		 return client.post().uri(uri)
				.headers((httpHeader) -> headers.keySet().forEach(key -> httpHeader.add(key, headers.get(key))))
				.body(body)
				.retrieve().toBodilessEntity();
	}
	
	public ResponseEntity<Employee> getRequestExpectingJsonResponse() throws URISyntaxException {
		URI uri = new URI("http://some-dummy-uri/data");
		Map<String,String> headers = new HashMap<String, String>();
		headers.put("Content-Type","application/json");
		headers.put("apiKey","12345");	
		
		RestClient client = builder.build();
		return client.get().uri(uri)
				.headers((httpHeader) -> headers.keySet().forEach(key -> httpHeader.add(key, headers.get(key))))
				.retrieve().toEntity(Employee.class);
	}
	

}
