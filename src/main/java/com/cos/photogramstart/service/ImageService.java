package com.cos.photogramstart.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.image.ImageRepository;
import com.cos.photogramstart.web.dto.image.ImageUploadDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImageService {
	
	private final ImageRepository imageRepository;
	
	@Value("${file.path}") // yml에 file path 값(C:/workspace/springbootwork/upload/)
	private String uploadFoler;
	
	@Transactional(readOnly = true)
	public List<Image> 이미지스토리(int principalId){
		List<Image> images = imageRepository.mStory(principalId);
		return images;
	}
	
	@Transactional
	public void 사진업로드(ImageUploadDto imageUploadDto,PrincipalDetails principalDetails) {
		UUID uuid = UUID.randomUUID(); // 랜덤으로 uuid 생성
		String imageFileName = uuid+"_"+imageUploadDto.getFile().getOriginalFilename(); // 실제 파일명 ex) apple.jpg
		Path imageFilePath = Paths.get(uploadFoler+imageFileName); //get안에는 실제 저장될 장소+파일명이 들어가야함 yml에 파일 경로 적어놓음 C:/workspace/springbootwork/upload/
		try {
			Files.write(imageFilePath, imageUploadDto.getFile().getBytes()); // 경로 안에 오리지널 파일을 바이트화 해서 넣음
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		// image 테이블에 저장
		Image image = imageUploadDto.toEntity(imageFileName,principalDetails.getUser());
		imageRepository.save(image);
		
	}
	
	
}
