package com.cos.photogramstart.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.ImageRepository;
import com.cos.photogramstart.web.dto.image.ImageUploadDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImageService {
	
	private final ImageRepository imageRepository;
	
	@Value("${file.path}") // yml에 file path 값(C:/workspace/springbootwork/upload/)
	private String uploadFoler;
	
	public void 사진업로드(ImageUploadDto imageUploadDto,PrincipalDetails principalDetails) {
		
		
		UUID uuid = UUID.randomUUID(); // 랜덤으로 uuid 생성
		String imageFileName = uuid+"_"+imageUploadDto.getFile().getOriginalFilename(); // 실제 파일명 ex) apple.jpg
		System.out.println("이미지 파일 이름 : "+imageFileName);
		
		Path imageFilePath = Paths.get(uploadFoler+imageFileName); //get안에는 실제 저장될 장소+파일명이 들어가야함 yml에 파일 경로 적어놓음 C:/workspace/springbootwork/upload/
		
		System.out.println("경로 : "+uploadFoler);
		System.out.println("imageFileName : "+imageFilePath);
		
		
		try {
			Files.write(imageFilePath, imageUploadDto.getFile().getBytes()); // 경로 안에 오리지널 파일을 바이트화 해서 넣음
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
}