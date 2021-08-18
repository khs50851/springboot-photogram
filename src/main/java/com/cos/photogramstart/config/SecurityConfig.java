package com.cos.photogramstart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cos.photogramstart.config.oauth.OAuth2DetailsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity // 이 파일로 시큐리티 활성화
@Configuration // IoC에 올림
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private final OAuth2DetailsService oAuth2DetailsService;
	
	@Bean
	public BCryptPasswordEncoder encode() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable(); // csrf토큰 비활성화
		
		http.authorizeRequests()
			.antMatchers("/","/user/**","/image/**","/subscribe/**","/comment/**","/api/**").authenticated() // 이건 권한 있어야 들어가짐
			.anyRequest().permitAll() // 나머진 접근 허가
			.and() 
			.formLogin() // 권한 있어야하는 페이지 접근하면 자동으로 로그인페이지 이동
			.loginPage("/auth/signin") // GET
			.loginProcessingUrl("/auth/signin") // POST -> 스프링 시큐리티가 로그인 프로세스 진행
			.defaultSuccessUrl("/") // 성공지 루트페이지로
			.and()
			.oauth2Login() // form 로그인도 하는데, oauth2로그인도 한다
			.userInfoEndpoint() // oauth2로그인을 하면 최종 응답을 코드로 받는게 아니라 바로 회원정보를 받을 수 있음
			.userService(oAuth2DetailsService);
			
	}
}
