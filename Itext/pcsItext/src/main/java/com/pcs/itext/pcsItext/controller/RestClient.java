package com.pcs.itext.pcsItext.controller;



import java.lang.reflect.Array;
import java.util.Arrays;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestClient {

	private static final String GET_DATA = "http://localhost:6050/test";
	
	static RestTemplate restTemplate = new RestTemplate();
	public static void main(String[] args) {
		callData();
		
	}
	
	private static void callData() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
		
		ResponseEntity<String> result = restTemplate.exchange(GET_DATA, HttpMethod.GET,entity, String.class);
		System.out.println(result);
	}
	
}
