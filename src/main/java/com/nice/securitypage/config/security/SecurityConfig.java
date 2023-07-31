package com.nice.securitypage.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationProvider provider;
    private final CustomAuthenticationFailureHandler failureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                    .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/admin")
                    .failureHandler(failureHandler)
                .and()
                .authorizeHttpRequests()
                    .requestMatchers("/main").permitAll()
                    .requestMatchers("/question").permitAll()
                    .requestMatchers("/login").permitAll()
                    .requestMatchers("/alreadyFin").permitAll()
                    .requestMatchers("/endPage").permitAll()
                    .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .invalidSessionUrl("/login")    // 세션이 유효하지 않을 때 리다이렉트 될 URL
                    .maximumSessions(1)  // 동시 세션 제한 수
                    .expiredUrl("/login")  // 세션이 만료될 때 리다이렉트 될 URL
                .and()
                    .sessionFixation().newSession()  // 로그인 할 때마다 새로운 세션을 생성
                .and()
                .authenticationProvider(provider)
                .build();

    }

//    @Bean
//    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
//        return new CustomAuthenticationFailureHandler();
//    }


}
