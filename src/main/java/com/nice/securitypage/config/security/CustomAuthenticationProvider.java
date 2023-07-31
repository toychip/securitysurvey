package com.nice.securitypage.config.security;

import com.nice.securitypage.entity.Manager;
import com.nice.securitypage.repository.ManagerRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final ManagerRepository managerRepository;
    private final HttpSession session;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String providedPassword = (String) authentication.getCredentials();

        // Encrypt the provided password
        String md5Password = EncryptionUtil.encryptMD5(providedPassword);
        String sha256Password = EncryptionUtil.encryptSHA256(md5Password);

        Manager manager = managerRepository.findByUsername(username)
                .orElse(null);

        if (manager == null || !manager.getPassword().equals(sha256Password)) {
            increaseFailureCount(username);
            throw new BadCredentialsException("아이디 혹은 비밀번호가 일치하지 않습니다.");
        }

        if (manager.isLocked()) {
            throw new LockedException("계정이 잠겼습니다.");
        }

        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        return new UsernamePasswordAuthenticationToken(username, providedPassword, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private void increaseFailureCount(String username) {
        Integer failureCount = (Integer) session.getAttribute(username + "-failureCount");
        if (failureCount == null) {
            failureCount = 1;
        } else {
            failureCount++;
        }

        session.setAttribute(username + "-failureCount", failureCount);

        // If the failure count is 5, lock the user
        if (failureCount >= 5) {
            Manager manager = managerRepository.findByUsername(username).orElse(null);
            if (manager != null) {
                lockUser(manager);
            }
        }
    }

    private void lockUser(Manager manager) {
        manager.lock();
        managerRepository.save(manager);
    }
}
