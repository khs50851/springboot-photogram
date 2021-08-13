package com.cos.photogramstart.web.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.subscribe.SubscribeService;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.service.UserService;
import com.cos.photogramstart.web.dto.CMRespDto;
import com.cos.photogramstart.web.dto.subscribe.SubscribeDto;
import com.cos.photogramstart.web.dto.user.UserUpdateDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserApiController {
	
	private final UserService userService;
	private final SubscribeService subscribeService;
	
	@PutMapping("/api/user/{principalId}/profileUrl")
	public ResponseEntity<?> profileUrlUpdate(@PathVariable int principalId,MultipartFile profileImageFile,@AuthenticationPrincipal PrincipalDetails principalDetails){ // 변수명을 form에 name값으로 적어야 받음
		User userEntity = userService.회원프로필사진변경(principalId,profileImageFile);
		principalDetails.setUser(userEntity); // 세션변경
		return new ResponseEntity<>(new CMRespDto<>(1,"프로필 사진 변경 성공",null),HttpStatus.OK);
		
	}
	
	@GetMapping("/api/user/{pageUserId}/subscribe")
	public ResponseEntity<?> subscribeList(@PathVariable int pageUserId,@AuthenticationPrincipal PrincipalDetails principalDetails){
		
		List<SubscribeDto> subscribeDto = subscribeService.구독리스트(principalDetails.getUser().getId(),pageUserId);
		
		return new ResponseEntity<>(new CMRespDto<>(1,"구독자 정보 불러오기 성공",subscribeDto),HttpStatus.OK);
		
	}
	
	
	@PutMapping("/api/user/{id}")
	public CMRespDto<?> update(@PathVariable int id,@Valid UserUpdateDto userUpdateDto,BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		if(bindingResult.hasErrors()) {
			Map<String, String> errorMap = new HashMap<>();
			
			for(FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
				// System.out.println(error.getDefaultMessage());
			}
			throw new CustomValidationApiException("유효성 검사 실패",errorMap);
		}else {
			System.out.println("userUpdateDto : "+userUpdateDto);
			User userEntity = userService.회원수정(id, userUpdateDto.toEntity());
			principalDetails.setUser(userEntity); // 세션정보 변경
			return new CMRespDto<>(1,"회원수정완료",userEntity);
		}
		
		
	}
}
