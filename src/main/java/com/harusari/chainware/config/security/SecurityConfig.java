package com.harusari.chainware.config.security;

import com.harusari.chainware.auth.jwt.JwtAuthenticationFilter;
import com.harusari.chainware.auth.jwt.JwtTokenProvider;
import com.harusari.chainware.auth.jwt.RestAccessDeniedHandler;
import com.harusari.chainware.auth.jwt.RestAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.harusari.chainware.config.security.SecurityPolicy.*;
import static com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType.*;
import static com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType.MASTER;
import static com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final RestAccessDeniedHandler restAccessDeniedHandler;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Master
                        .requestMatchers(HttpMethod.GET, MASTER_ONLY_URLS).hasAuthority(MASTER.name())
                        .requestMatchers(HttpMethod.POST, MASTER_ONLY_URLS).hasAuthority(MASTER.name())
                        .requestMatchers(HttpMethod.PUT, MASTER_ONLY_URLS).hasAuthority(MASTER.name())
                        .requestMatchers(HttpMethod.DELETE, MASTER_ONLY_URLS).hasAuthority(MASTER.name())

                        // GENERAL_MANAGER
                        .requestMatchers(HttpMethod.GET, SecurityPolicy.GENERAL_MANAGER_URLS).hasAuthority(GENERAL_MANAGER.name())
                        .requestMatchers(HttpMethod.POST, SecurityPolicy.GENERAL_MANAGER_URLS).hasAuthority(GENERAL_MANAGER.name())
                        .requestMatchers(HttpMethod.PUT, SecurityPolicy.GENERAL_MANAGER_URLS).hasAuthority(GENERAL_MANAGER.name())
                        .requestMatchers(HttpMethod.DELETE, SecurityPolicy.GENERAL_MANAGER_URLS).hasAuthority(GENERAL_MANAGER.name())

                        // SENIOR_MANAGER
                        .requestMatchers(HttpMethod.GET, SecurityPolicy.SENIOR_MANAGER_URLS).hasAuthority(SENIOR_MANAGER.name())
                        .requestMatchers(HttpMethod.POST, SecurityPolicy.SENIOR_MANAGER_URLS).hasAuthority(SENIOR_MANAGER.name())
                        .requestMatchers(HttpMethod.PUT, SecurityPolicy.SENIOR_MANAGER_URLS).hasAuthority(SENIOR_MANAGER.name())
                        .requestMatchers(HttpMethod.DELETE, SecurityPolicy.SENIOR_MANAGER_URLS).hasAuthority(SENIOR_MANAGER.name())

                        // FRANCHISE_MANAGER
                        .requestMatchers(FRANCHISE_MANAGER_URLS).hasAuthority(FRANCHISE_MANAGER.name())

                        // WAREHOUSE_MANAGER
                        .requestMatchers(WAREHOUSE_MANAGER_URLS).hasAuthority(WAREHOUSE_MANAGER.name())

                        // Public (permitAll)
                        .requestMatchers(HttpMethod.POST, PUBLIC_URLS).permitAll()

                        // Authenticated
                        .requestMatchers(HttpMethod.GET, AUTHENTICATED_URLS).authenticated()
                        .requestMatchers(HttpMethod.POST, AUTHENTICATED_URLS).authenticated()
                        .requestMatchers(HttpMethod.PUT, AUTHENTICATED_URLS).authenticated()
                        .requestMatchers(HttpMethod.DELETE, AUTHENTICATED_URLS).authenticated()

                        // 그 외 모든 요청 및 HTTP method 차단
                        .anyRequest().denyAll()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(restAuthenticationEntryPoint)
                        .accessDeniedHandler(restAccessDeniedHandler)
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService);
    }

}