package com.cos.photogramstart.domain.image;


import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

import com.cos.photogramstart.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity // 디비에 테이블 생성
public class Image {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //번호 증가 전략을 데이터베이스에 따라가게함
	private int id;
	
	private String caption;
	private String postImageUrl; // 사진을 전송 받아서 그 사진을 서버에 특정 폴더에 저장 - DB에는 그 저장된 경로를 insert
	
	@JsonIgnoreProperties({"images"}) // User에 있는 images로인한 무한참조 방지
	@JoinColumn(name = "userId")
	@ManyToOne
	private User user;
	
	private LocalDateTime createDate;
	
	@PrePersist // 디비에서 insert되기 직전에 실행
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
}
