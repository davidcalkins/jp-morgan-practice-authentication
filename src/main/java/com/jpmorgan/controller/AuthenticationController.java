package com.jpmorgan.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jpmorgan.beans.UserCredentials;
import com.jpmorgan.beans.UserToken;
import com.jpmorgan.service.AuthenticationService;

@CrossOrigin
@RestController
@RequestMapping("/authors")
public class AuthenticationController {
	
	@Autowired
	AuthenticationService authService;
	
	@PostMapping(value="/login", consumes=MediaType.APPLICATION_JSON_VALUE)
	public UserToken login(@RequestBody UserCredentials userCredentials) 
	{
		
		
		return authService.login(userCredentials);
	}
	
	@PostMapping(value="/logout", consumes=MediaType.APPLICATION_JSON_VALUE)
	public void logout(@RequestBody UserToken userToken) 
	{
		authService.logout(userToken);
	}
	
	@PostMapping(value="/validate", consumes=MediaType.APPLICATION_JSON_VALUE)
	public boolean validate(@RequestBody UserToken userToken)
	{
		return authService.isValid(userToken);
	}
	
	@ExceptionHandler
	public void handleException(Exception ex, HttpServletResponse response) {
		try {
			PrintWriter responseWriter = response.getWriter();
			ex.printStackTrace(responseWriter);
			
		} catch (IOException ioex) {
			/* Nothing to do if this fails */
		} finally {
			try {
				response.sendError(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			} catch(IOException ioex) {/* Nothing to do here either */}
		}
	}
	
}
