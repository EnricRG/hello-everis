package com.everis.hello.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	
	@RequestMapping(value = "/hello",
	        produces = { "application/json" }, 
	        method = RequestMethod.GET)
	public ResponseEntity<String> sayHello(@RequestParam(value = "name", required = false) String name){
		return new ResponseEntity<String>("Hello "+ (StringUtils.isBlank(name) ? "unkown" : name) , HttpStatus.OK);
	}

}
