package com.nice.securitypage.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .formLogin()
                .defaultSuccessUrl("/admin")
                .and()
                .authorizeHttpRequests()
                    .requestMatchers("/main").permitAll()
                    .requestMatchers("/question").permitAll()
//                    .requestMatchers("/login").permitAll()
                    .requestMatchers("/alreadyFin").permitAll()
                    .requestMatchers("/endPage").permitAll()
                    .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()

                .anyRequest().authenticated()
                    .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .build();

    }


}
