package com.harusari.chainware.member.query.service;

import com.harusari.chainware.exception.auth.MemberNotFoundException;
import com.harusari.chainware.member.command.application.dto.response.EmailExistsResponse;
import com.harusari.chainware.member.query.dto.request.MemberSearchRequest;
import com.harusari.chainware.member.query.dto.response.MemberSearchDetailResponse;
import com.harusari.chainware.member.query.dto.response.MemberSearchResponse;
import com.harusari.chainware.member.query.repository.MemberQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.harusari.chainware.exception.auth.AuthErrorCode.MEMBER_NOT_FOUND_EXCEPTION;
import static com.harusari.chainware.member.common.constants.EmailValidationConstant.EMAIL_VALIDATION_PREFIX;
import static com.harusari.chainware.member.common.constants.EmailValidationConstant.EMAIL_VALIDATION_TTL;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryServiceImpl implements MemberQueryService {

    private final MemberQueryRepository memberQueryRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public EmailExistsResponse checkEmailDuplicate(String email) {
        if (memberQueryRepository.existsByEmail(email)) {
            return EmailExistsResponse.builder()
                    .exists(true)
                    .validationToken(null)
                    .build();
        }

        String token = generateAndStoreEmailValidationToken(email);

        return EmailExistsResponse.builder()
                .exists(false)
                .validationToken(token)
                .build();
    }

    @Override
    public Page<MemberSearchResponse> searchMembers(MemberSearchRequest memberSearchRequest, Pageable pageable) {
        return memberQueryRepository.findMembers(memberSearchRequest, pageable);
    }

    @Override
    public MemberSearchDetailResponse getMemberDetail(Long memberId) {
        return memberQueryRepository.findMemberSearchDetailById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND_EXCEPTION));
    }

    private String generateAndStoreEmailValidationToken(String email) {
        String token = UUID.randomUUID().toString();
        String redisKey = EMAIL_VALIDATION_PREFIX + token;
        redisTemplate.opsForValue().set(redisKey, email, EMAIL_VALIDATION_TTL);
        return token;
    }

}