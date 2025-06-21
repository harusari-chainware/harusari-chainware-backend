package com.harusari.chainware.auth.jwt;

import com.harusari.chainware.exception.auth.ExpiredJwtTokenException;
import com.harusari.chainware.exception.auth.EmptyJwtClaimsException;
import com.harusari.chainware.exception.auth.InvalidJwtTokenException;
import com.harusari.chainware.exception.auth.JwtTokenEmptyException;
import com.harusari.chainware.exception.auth.UnsupportedJwtTokenException;
import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

import static com.harusari.chainware.exception.auth.AuthErrorCode.*;

@Component
public class JwtTokenProvider {

    private static final String CLAIM_AUTHORITY = "authority";

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt.refresh-expiration}")
    private long jwtRefreshExpiration;

    private SecretKey secretKey;

    @PostConstruct
    public void initSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(String email, MemberAuthorityType memberAuthorityType) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .subject(email)
                .claim(CLAIM_AUTHORITY, memberAuthorityType)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(String email, MemberAuthorityType memberAuthorityType) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtRefreshExpiration);

        return Jwts.builder()
                .subject(email)
                .claim(CLAIM_AUTHORITY, memberAuthorityType)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    public long getRefreshExpiration() {
        return jwtRefreshExpiration;
    }

    public boolean validateToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new JwtTokenEmptyException(JWT_TOKEN_EMPTY_EXCEPTION);
        }

        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new InvalidJwtTokenException(INVALID_JWT_TOKEN_EXCEPTION);
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtTokenException(EXPIRED_JWT_TOKEN_EXCEPTION);
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtTokenException(UNSUPPORTED_JWT_TOKEN_EXCEPTION);
        } catch (IllegalArgumentException e) {
            throw new EmptyJwtClaimsException(EMPTY_JWT_CLAIMS_EXCEPTION);
        }
    }

    public String getEmailFromJWT(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

}