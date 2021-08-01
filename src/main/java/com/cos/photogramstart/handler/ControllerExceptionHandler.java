package com.cos.photogramstart.handler;

import java.util.Map;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.cos.photogramstart.handler.ex.CustomValidationException;

@RestController
@ControllerAdvice // exception error 가로채기
public class ControllerExceptionHandler {
	
	@ExceptionHandler(CustomValidationException.class) // runtime exception 발생할경우 
	public Map<String,String> validationException(CustomValidationException e) {
		return e.getErrorMap();
	}
}
