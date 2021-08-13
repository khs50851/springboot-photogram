package com.cos.photogramstart.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.web.dto.user.UserProfileDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	
	private final UserRepository userRepository;
	private final SubscribeRepository subscribeRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Value("${file.path}")
	private String uploadFoler;
	
	@Transactional
	public User 회원프로필사진변경(int principalId,MultipartFile profileImageFile) {
		UUID uuid = UUID.randomUUID(); // 랜덤으로 uuid 생성
		String imageFileName = uuid+"_"+profileImageFile.getOriginalFilename(); // 실제 파일명 ex) apple.jpg
		Path imageFilePath = Paths.get(uploadFoler+imageFileName); //get안에는 실제 저장될 장소+파일명이 들어가야함 yml에 파일 경로 적어놓음 C:/workspace/springbootwork/upload/
		try {
			Files.write(imageFilePath, profileImageFile.getBytes()); // 경로 안에 오리지널 파일을 바이트화 해서 넣음
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		User userEntity = userRepository.findById(principalId).orElseThrow(()->{
			throw new CustomApiException("유저를 찾을 수 없습니다.");
		});
		userEntity.setProfileImageUrl(imageFileName);
		
		return userEntity;
	} // 더티체킹으로 업데이트
	
	@Transactional(readOnly = true)
	public UserProfileDto 회원프로필(int pageUserId,int principalId) {
		UserProfileDto dto = new UserProfileDto();
		
		// select * from image where userId=:userid;
		User userEntity = userRepository.findById(pageUserId).orElseThrow(()->{
			throw new CustomException("해당 프로필 페이지는 없는 페이지입니다.");
		});
		
		dto.setUser(userEntity);
		dto.setPageOwnerState(pageUserId == principalId); // 1은 페이지 주인, -1은 주인 x 
		dto.setImageCount(userEntity.getImages().size());
		int subState = subscribeRepository.mSubscribeState(principalId, pageUserId);
		int subCount = subscribeRepository.mSubscribeCount(pageUserId);
		
		dto.setSubscribeState(subState==1);
		dto.setSubscribeCount(subCount);
		
		// 회원 프로필에서의 좋아요 카운트
		userEntity.getImages().forEach((image)->{
			image.setLikeCount(image.getLikes().size());
		});
		
		return dto;
	}
	
	@Transactional
	public User 회원수정(int id,User user) {
		// 영속화
		User userEntity = userRepository.findById(id).orElseThrow(()->{return new CustomValidationApiException("찾을 수 없는 id입니다.");});
		
		// 오브젝트 수정 - 더티체킹
		userEntity.setName(user.getName());
		
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		
		userEntity.setPassword(encPassword);
		userEntity.setBio(user.getBio());
		userEntity.setWebsite(user.getWebsite());
		userEntity.setPhone(user.getPhone());
		userEntity.setGender(user.getGender());
		
		return userEntity;
	}
	
	
}
