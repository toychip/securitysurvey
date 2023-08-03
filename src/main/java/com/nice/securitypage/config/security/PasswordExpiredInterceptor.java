package com.nice.securitypage.config.security;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class PasswordExpiredInterceptor implements HandlerInterceptor {

    // 세션에서 passwordExpired 값을 가져옴
    // null일 경우 자동으로 false
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Boolean passwordExpired = (Boolean) request.getSession().getAttribute("passwordExpired");

        // uri입력 접속 방지
        // Null을 제외한 필드에서 Boolean값 비교
        if (Boolean.TRUE.equals(passwordExpired) && request.getRequestURI().startsWith("/admin")) {
            response.sendRedirect("/change-password");

            // 요청을 중단하기 위해 false
            return false;
        }

        // 정상로직
        return true;
    }
}
