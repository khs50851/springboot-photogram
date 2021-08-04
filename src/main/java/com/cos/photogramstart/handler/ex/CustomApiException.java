package com.cos.photogramstart.handler.ex;

import java.util.Map;

public class CustomApiException extends RuntimeException{
	
	// 객체를 구분할때
	private static final long serialVersionUID = 1L;
	
	public CustomApiException(String message) {
		super(message);
	}
	
	
}
