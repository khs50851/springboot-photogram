package com.cos.photogramstart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity // 이 파일로 시큐리티 활성화
@Configuration // IoC에 올림
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Bean
	public BCryptPasswordEncoder encode() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable(); // csrf토큰 비활성화
		
		http.authorizeRequests()
			.antMatchers("/","/user/**","/image/**","/subscribe/**","/comment/**").authenticated() // 이건 권한 있어야 들어가짐
			.anyRequest().permitAll() // 나머진 접근 허가
			.and() 
			.formLogin() // 권한 있어야하는 페이지 접근하면 자동으로 로그인페이지 이동
			.loginPage("/auth/signin")
			.defaultSuccessUrl("/");
			
	}
}
