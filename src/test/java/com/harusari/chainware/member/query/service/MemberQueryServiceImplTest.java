package com.harusari.chainware.member.query.service;

import com.harusari.chainware.member.command.application.dto.response.EmailExistsResponse;
import com.harusari.chainware.member.query.repository.MemberQueryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static com.harusari.chainware.member.common.constants.EmailValidationConstant.EMAIL_VALIDATION_PREFIX;
import static com.harusari.chainware.member.common.constants.EmailValidationConstant.EMAIL_VALIDATION_TTL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("[회원 - query service] MemberQueryServiceImpl 테스트")
class MemberQueryServiceImplTest {

    @InjectMocks
    private MemberQueryServiceImpl memberQueryService;

    @Mock
    private MemberQueryRepository memberQueryRepository;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Test
    @DisplayName("[회원 이메일 중복O] 회원가입 시 중복된 이메일이면 토큰을 발급하지 않는 테스트")
    void testCheckEmailDuplicateNotToken() {
        String email = "test@harusari.com";

        when(memberQueryRepository.existsByEmail(email)).thenReturn(true);

        EmailExistsResponse emailExistsResponse = memberQueryService.checkEmailDuplicate(email);

        assertThat(emailExistsResponse).isNotNull();
        assertThat(emailExistsResponse.exists()).isTrue();
        assertThat(emailExistsResponse.validationToken()).isNull();
    }

    @Test
    @DisplayName("[회원 이메일 중복X] 회원가입 시 중복되지 않은 이메일임녀 토큰을 발급하는 테스트")
    void testCheckEmailDuplicate() {
        String email = "test@harusari.com";

        when(memberQueryRepository.existsByEmail(email)).thenReturn(false);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        EmailExistsResponse emailExistsResponse = memberQueryService.checkEmailDuplicate(email);

        assertThat(emailExistsResponse).isNotNull();
        assertThat(emailExistsResponse.exists()).isFalse();
        assertThat(emailExistsResponse.validationToken()).isNotNull();

        verify(valueOperations, times(1)).set(startsWith(EMAIL_VALIDATION_PREFIX), eq(email), eq(EMAIL_VALIDATION_TTL));
    }

}