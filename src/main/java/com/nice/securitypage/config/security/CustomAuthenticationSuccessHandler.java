package com.nice.securitypage.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 인증 객체에서 비밀번호 만료 상태를 확인
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getDetails();
        Boolean passwordExpired = userDetails.getPasswordExpired();

        logger.info("CustomAuthenticationSuccessHandler - Password expired: " + passwordExpired);

        if (passwordExpired != null && passwordExpired) {
            // 비밀번호가 만료된 경우 비밀번호 변경 페이지로 리다이렉트
            response.sendRedirect("/change-password");
        } else {
            // 비밀번호가 만료되지 않은 경우 기본 리다이렉트 로직을 수행 (예: 관리자 페이지로 리다이렉트)
            response.sendRedirect("/admin");
        }
    }
}

