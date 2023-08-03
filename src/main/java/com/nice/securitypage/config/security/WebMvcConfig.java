package com.nice.securitypage.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final PasswordExpiredInterceptor passwordExpiredInterceptor;


    // Spring MVC의 설정을 커스터마이징
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passwordExpiredInterceptor) // 비밀번호가 유효한가?
                .addPathPatterns("/admin/**");  // 관리자의 모든 페이지는 가로챔
    }
}
