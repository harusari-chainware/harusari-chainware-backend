package com.harusari.chainware.auth.service;

import com.harusari.chainware.auth.dto.RefreshTokenDTO;
import com.harusari.chainware.auth.dto.request.LoginRequest;
import com.harusari.chainware.auth.dto.response.TokenResponse;
import com.harusari.chainware.auth.jwt.JwtTokenProvider;
import com.harusari.chainware.exception.auth.*;
import com.harusari.chainware.member.command.domain.aggregate.Authority;
import com.harusari.chainware.member.command.domain.aggregate.LoginHistory;
import com.harusari.chainware.member.command.domain.aggregate.Member;
import com.harusari.chainware.member.command.domain.repository.AuthorityCommandRepository;
import com.harusari.chainware.member.command.domain.repository.LoginHistoryCommandRepository;
import com.harusari.chainware.member.query.repository.MemberQueryRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Date;

import static com.harusari.chainware.exception.auth.AuthErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final String HEADER_X_FORWARDED_FOR = "X-Forwarded-For";
    private static final String HEADER_USER_AGENT = "User-Agent";
    private static final String IP_DELIMITER = ",";

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberQueryRepository memberQueryRepository;
    private final AuthorityCommandRepository authorityCommandRepository;
    private final LoginHistoryCommandRepository loginHistoryCommandRepository;
    private final RedisTemplate<String, RefreshTokenDTO> refreshTokenRedisTemplate;

    @Transactional
    @Override
    public TokenResponse login(LoginRequest loginRequest, HttpServletRequest httpServletRequest) {
        Member member = findAndValidateMember(loginRequest);
        Authority authority = loadAuthority(member.getAuthorityId());

        TokenResponse tokenResponse = generateTokens(member, authority);
        storeRefreshToken(member.getEmail(), tokenResponse.refreshToken());

        saveLoginHistory(member, httpServletRequest);

        return tokenResponse;
    }

    @Override
    public TokenResponse refreshToken(String providedRefreshToken) {
        jwtTokenProvider.validateToken(providedRefreshToken);
        String email = jwtTokenProvider.getEmailFromJWT(providedRefreshToken);

        RefreshTokenDTO storedRefreshToken = getStoredRefreshToken(email);
        validateRefreshToken(providedRefreshToken, storedRefreshToken);

        Member member = findMemberByEmail(email);
        Authority authority = loadAuthority(member.getAuthorityId());

        TokenResponse tokenResponse = generateTokens(member, authority);
        storeRefreshToken(email, tokenResponse.refreshToken());
        return tokenResponse;
    }

    @Override
    public void logout(String refreshToken) {
        jwtTokenProvider.validateToken(refreshToken);
        String email = jwtTokenProvider.getEmailFromJWT(refreshToken);
        refreshTokenRedisTemplate.delete(email);
    }

    private Member findAndValidateMember(LoginRequest loginRequest) {
        Member member = findMemberByEmail(loginRequest.email());
        if (!passwordEncoder.matches(loginRequest.password(), member.getPassword())) {
            throw new InvalidCredentialsException(INVALID_CREDENTIALS_EXCEPTION);
        }
        return member;
    }

    private Member findMemberByEmail(String email) {
        return memberQueryRepository.findActiveMemberByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND_EXCEPTION));
    }

    private Authority loadAuthority(Integer authorityId) {
        return authorityCommandRepository.findByAuthorityId(authorityId);
    }

    private TokenResponse generateTokens(Member member, Authority authority) {
        String accessToken = jwtTokenProvider.createToken(member.getEmail(), authority.getAuthorityName());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getEmail(), authority.getAuthorityName());
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void storeRefreshToken(String email, String refreshToken) {
        RefreshTokenDTO refreshTokenDTO = RefreshTokenDTO.builder()
                .email(email)
                .refreshToken(refreshToken)
                .expiryDate(new Date(System.currentTimeMillis() + jwtTokenProvider.getRefreshExpiration()))
                .build();

        refreshTokenRedisTemplate.opsForValue().set(
                email, refreshTokenDTO, Duration.ofDays(jwtTokenProvider.getRefreshExpiration())
        );
    }

    private RefreshTokenDTO getStoredRefreshToken(String email) {
        return refreshTokenRedisTemplate.opsForValue().get(email);
    }

    private void validateRefreshToken(String providedRefreshToken, RefreshTokenDTO storedRefreshToken) {
        if (storedRefreshToken == null) {
            throw new RefreshTokenNotFoundException(REFRESH_TOKEN_NOT_FOUND_EXCEPTION);
        }

        if (!storedRefreshToken.refreshToken().equals(providedRefreshToken)) {
            throw new RefreshTokenMismatchException(REFRESH_TOKEN_MISMATCH_EXCEPTION);
        }

        if (storedRefreshToken.expiryDate().before(new Date())) {
            throw new RefreshTokenExpiredException(REFRESH_TOKEN_EXPIRED_EXCEPTION);
        }
    }

    private void saveLoginHistory(Member member, HttpServletRequest request) {
        String ipAddress = extractClientIp(request);
        String browser = request.getHeader(HEADER_USER_AGENT);

        LoginHistory loginHistory = LoginHistory.builder()
                .memberId(member.getMemberId())
                .ipAddress(ipAddress)
                .browser(browser)
                .build();

        loginHistoryCommandRepository.save(loginHistory);
    }

    private String extractClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader(HEADER_X_FORWARDED_FOR);
        return forwarded != null ? forwarded.split(IP_DELIMITER)[0].trim() : request.getRemoteAddr();
    }

}