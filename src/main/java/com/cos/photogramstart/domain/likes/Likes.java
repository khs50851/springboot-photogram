package com.cos.photogramstart.domain.likes;


import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.cos.photogramstart.domain.image.Image;
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
@Table(
		uniqueConstraints = {
				@UniqueConstraint(
						name="likes_uk",
						columnNames = {"imageId","userId"}
						)
		}				
		)

public class Likes { // N
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //번호 증가 전략을 데이터베이스에 따라가게함
	private int id;
	
	@JsonIgnoreProperties({"likes"})
	@JoinColumn(name = "imageId")
	@ManyToOne
	private Image image; //1
	
	@JsonIgnoreProperties({"images"})
	@JoinColumn(name = "userId")
	@ManyToOne
	private User user; //1
	
	private LocalDateTime createDate;
	
	@PrePersist // 디비에서 insert되기 직전에 실행
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
	
}
