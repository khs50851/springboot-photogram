package com.cos.photogramstart.handler.ex;

import java.util.Map;

public class CustomException extends RuntimeException{
	
	// 객체를 구분할때
	private static final long serialVersionUID = 1L;
	
	
	public CustomException(String message) {
		super(message);
	}
	
}
