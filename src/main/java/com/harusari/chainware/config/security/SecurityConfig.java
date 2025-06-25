package com.harusari.chainware.config.security;

import com.harusari.chainware.auth.jwt.JwtAuthenticationFilter;
import com.harusari.chainware.auth.jwt.JwtTokenProvider;
import com.harusari.chainware.auth.jwt.RestAccessDeniedHandler;
import com.harusari.chainware.auth.jwt.RestAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
                .authorizeHttpRequests(auth -> {
                    for (SecurityPolicy policy : SecurityPolicy.values()) {
                        switch (policy.getAccessType()) {
                            case PERMIT_ALL -> auth
                                    .requestMatchers(policy.getMethod(), policy.getPath())
                                    .permitAll();
                            case AUTHENTICATED -> auth
                                    .requestMatchers(policy.getMethod(), policy.getPath())
                                    .authenticated();
                            case ROLE_BASED -> auth
                                    .requestMatchers(policy.getMethod(), policy.getPath())
                                    .hasAnyAuthority(
                                            policy.getMemberAuthorities().stream()
                                                    .map(Enum::name)
                                                    .toArray(String[]::new)
                                    );
                        }
                    }
                    auth.anyRequest().denyAll();
                })
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