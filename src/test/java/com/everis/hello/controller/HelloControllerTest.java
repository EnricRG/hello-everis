package com.everis.hello.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloControllerTest {
	
	@InjectMocks
	private HelloController helloController;

	@BeforeEach
	public void onBefore() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void sayHello_shouldReturnNameOK() {
		String name = "Aitor";
		
		ResponseEntity<String> response = helloController.sayHello(name);
		
		assertEquals("Hello " + name, response.getBody());
		
	}
	
	@Test
	public void sayHello_shouldReturnUnkownOK() {
		
		ResponseEntity<String> response = helloController.sayHello(null);
		
		assertEquals("Hello unkown", response.getBody());
		
	}

}
