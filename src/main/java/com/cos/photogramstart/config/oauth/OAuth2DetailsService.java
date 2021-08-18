package com.cos.photogramstart.config.oauth;

import java.util.Map;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OAuth2DetailsService extends DefaultOAuth2UserService{ // facebook로그인시 응답을 처리하는 곳
	
	private final UserRepository userRepository;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		Map<String, Object> userInfo = oAuth2User.getAttributes();
		String name = (String)userInfo.get("name");
		String email = (String)userInfo.get("email");
		String username = "facebook_"+(String)userInfo.get("id");
		String password =  new BCryptPasswordEncoder().encode(UUID.randomUUID().toString());
		
		User userEntity = userRepository.findByUsername(username);
		
		if(userEntity == null) { // facebook 최초 로그인
			User user = User.builder()
					.username(username)
					.password(password)
					.name(name)
					.email(email)
					.role("ROLE_USER")
					.build();
				
			return new PrincipalDetails(userRepository.save(user));
		}else { // 이미 로그인 해봄
			return new PrincipalDetails(userEntity);
		}
		
	}
}
