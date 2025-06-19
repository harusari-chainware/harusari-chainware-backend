package com.harusari.chainware.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 처리 비활성화 (default가 활성화이므로 작성해줘야 함)
        return http.csrf(AbstractHttpConfigurer::disable)
                // 세션 로그인 x -> 토큰 로그인 설정으로 진행
                .sessionManagement(session
                        -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 요청 http method, url 기준으로 인증, 인가 필요 여부 설정
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(HttpMethod.POST, "/api/v1/members/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/members/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/requisitions/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/requisitions/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/v1/requisitions/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/orders/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/orders/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/v1/orders/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/delivery/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/delivery/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/v1/delivery/**").permitAll()
                                .anyRequest().authenticated()
                ).build();
    }

}