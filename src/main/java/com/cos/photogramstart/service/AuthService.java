package com.cos.photogramstart.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service // Ioc 등록 및 트랜잭션 관리
public class AuthService {
	
	
	
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final UserRepository userRepository;
	
	@Transactional
	public User 회원가입(User user) {
		
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword); // 패스워드 암호화
		user.setPassword(encPassword);
		user.setRole("ROLE_USER"); // 권한은 일반 유저로 설정
		User userEntity = userRepository.save(user);
		
		return userEntity;
		
	}
	
	
}
