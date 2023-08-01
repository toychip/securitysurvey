package com.nice.securitypage.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

// 실패한 인증 시도를 처리하는 클래스
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    // 사용자가 인증에 실패했을 때 호출되는 메서드
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage;
        if (exception instanceof LockedException) {
            errorMessage = "5번 잘못 입력했기 때문에 계정이 잠겼습니다.";
        } else if (exception instanceof BadCredentialsException) {
            errorMessage = "아이디 혹은 비밀번호가 일치하지 않습니다.";
        } else if (exception instanceof CredentialsExpiredException) {
            response.sendRedirect("/change-password");
            return;
        } else {
            errorMessage = "로그인 실패";
        }

        // 세션에 오류 메시지를 설정하여 사용자에게 표시 가능하게 함
        request.getSession().setAttribute("errorMessage", errorMessage);
        // 사용자를 로그인 페이지로 다시 리다이렉트
        response.sendRedirect("/login");
    }
}
