package com.cos.photogramstart.web.dto.image;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ImageUploadDto {
	
	private MultipartFile file; // 파일 받기
	private String caption;
	
	
}
