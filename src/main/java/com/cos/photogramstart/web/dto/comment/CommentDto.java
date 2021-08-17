package com.cos.photogramstart.web.dto.comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.sun.istack.NotNull;

import lombok.Data;

@Data
public class CommentDto {
	
	@NotBlank // 빈값, null, 공백 ""
	private String content;
	@NotNull //null값 체크 (int타입은 notnull로)
	private Integer imageId;
}
