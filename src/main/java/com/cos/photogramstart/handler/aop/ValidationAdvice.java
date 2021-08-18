package com.cos.photogramstart.handler.aop;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;

@Component // restcontroller, service이 모든것들이 component의 구현체임
@Aspect
public class ValidationAdvice {
	
	private static final Logger log = LoggerFactory.getLogger(ValidationAdvice.class);
	
	@Around("execution(* com.cos.photogramstart.web.api.*Controller.*(..))")
	public Object apiAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		
		String type = proceedingJoinPoint.getSignature().getDeclaringTypeName();
		String method = proceedingJoinPoint.getSignature().getName();
		
		Object[] args = proceedingJoinPoint.getArgs(); // 함수의 매개변수를 배열로 뽑기
		
		for(Object arg : args) { 
			if(arg instanceof BindingResult) {
				BindingResult bindingResult = (BindingResult) arg;
				if(bindingResult.hasErrors()) {
					Map<String,String> errorMap = new HashMap<>();
					
					for(FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
					log.warn(type+"."+method+"() => 필드 : "+error.getField()+", 메세지 : "+error.getDefaultMessage());	
					}
					throw new CustomValidationApiException("유효성 검사 실패", errorMap);
				}
			}
		}
		
		// 이건 항상 설정된 범위의 함수보다 먼저 실행됨
		// proceedingJoinPoint 어떤 함수의 모든곳에 접근할 수 있는 매개변수
		
		return proceedingJoinPoint.proceed(); // 원래의 함수로 돌아감
	}
	
	@Around("execution(* com.cos.photogramstart.web.*Controller.*(..))")
	public Object advice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		
		String type = proceedingJoinPoint.getSignature().getDeclaringTypeName();
		String method = proceedingJoinPoint.getSignature().getName();
		
		Object[] args = proceedingJoinPoint.getArgs();
		
		for(Object arg : args) {
			if(arg instanceof BindingResult) {
				BindingResult bindingResult = (BindingResult) arg;
				
				if(bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap<>();
					for(FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
						log.warn(type+"."+method+"() => 필드 : "+error.getField()+", 메세지 : "+error.getDefaultMessage());
					}
					throw new CustomValidationException("유효성 검사 실패",errorMap);
				}
				
			}
		}
		
		return proceedingJoinPoint.proceed();
	}
}
