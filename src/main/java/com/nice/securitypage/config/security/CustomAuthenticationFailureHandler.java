package com.nice.securitypage.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage;
        if (exception instanceof LockedException) {
            errorMessage = "5번 잘못 입력했기 때문에 계정이 잠겼습니다.";
        } else if (exception instanceof BadCredentialsException) {
            errorMessage = "아이디 혹은 비밀번호가 일치하지 않습니다.";
        } else {
            errorMessage = "로그인 실패";
        }

        request.getSession().setAttribute("errorMessage", errorMessage);
        response.sendRedirect("/login");
    }
}
