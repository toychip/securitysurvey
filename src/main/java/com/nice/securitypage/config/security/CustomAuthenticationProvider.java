package com.nice.securitypage.config.security;

import com.nice.securitypage.entity.Manager;
import com.nice.securitypage.repository.ManagerRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
// 인증 제공자는 사용자가 입력한 아이디와 비밀번호를 검증하고 인증된 사용자를 반환하는 역할을 수행
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final ManagerRepository managerRepository;
    private final HttpSession session;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String providedPassword = (String) authentication.getCredentials();

        // 입력된 비밀번호를 암호화
        String md5Password = EncryptionUtil.encryptMD5(providedPassword);
        String sha256Password = EncryptionUtil.encryptSHA256(md5Password);

        // 아이디로 사용자 정보를 조회
        Manager manager = managerRepository.findByUsername(username)
                .orElse(null);

        // 아이디나 비밀번호가 일치하지 않으면 실패 횟수를 증가시키고 예외를 발생시킴
        if (manager == null || !manager.getPassword().equals(sha256Password)) {
            increaseFailureCount(username);
            throw new BadCredentialsException("아이디 혹은 비밀번호가 일치하지 않습니다.");
        }

        // 계정이 잠겨있으면 예외를 발생시킴
        if (manager.isLocked()) {
            throw new LockedException("계정이 잠겼습니다.");
        }

        // HttpServletRequest 객체를 통해 세션을 얻음
        // 사용자의 권한을 설정하고 인증된 사용자를 반환
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, providedPassword, authorities);

        if (sha256Password.equals("11111111") || manager.getCreatedDate().plusDays(90).isBefore(LocalDateTime.now())) {
            auth.setDetails(new CustomUserDetails(true));
        } else {
            auth.setDetails(new CustomUserDetails(false));
        }

        return auth;

    }

    @Override
    // 인증 제공자가 지정된 인증 방식을 지원하는지 확인
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    // 실패 횟수를 증가시키는 메서드
    private void increaseFailureCount(String username) {
        Integer failureCount = (Integer) session.getAttribute(username + "-failureCount");
        if (failureCount == null) {
            failureCount = 1;
        } else {
            failureCount++;
        }

        session.setAttribute(username + "-failureCount", failureCount);

        // 실패 횟수가 5번 이상이면 계정을 잠금
        if (failureCount >= 5) {
            Manager manager = managerRepository.findByUsername(username).orElse(null);
            if (manager != null) {
                lockUser(manager);
            }
        }
    }

    // 사용자 계정을 잠그는 메서드
    private void lockUser(Manager manager) {
        manager.lock();
        managerRepository.save(manager);
    }
}
