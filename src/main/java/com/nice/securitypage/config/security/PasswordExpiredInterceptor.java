package com.nice.securitypage.config.security;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class PasswordExpiredInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Boolean passwordExpired = (Boolean) request.getSession().getAttribute("passwordExpired");
        if (Boolean.TRUE.equals(passwordExpired) && request.getRequestURI().startsWith("/admin")) {
            response.sendRedirect("/change-password");
            return false;
        }

        return true;
    }
}
