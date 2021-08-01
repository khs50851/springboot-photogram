package com.cos.photogramstart.handler;

import java.util.Map;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.util.Script;
import com.cos.photogramstart.web.dto.CMRespDto;

@RestController
@ControllerAdvice // exception error 가로채기
public class ControllerExceptionHandler {
	
	@ExceptionHandler(CustomValidationException.class) // runtime exception 발생할경우 
	public String validationException(CustomValidationException e) {
		return Script.back(e.getErrorMap().toString());
	}
}
