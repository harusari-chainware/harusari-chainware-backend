package com.harusari.chainware.member.command.application.service;

import com.harusari.chainware.exception.member.EmailAlreadyExistsException;
import com.harusari.chainware.exception.member.EmailVerificationRequiredException;
import com.harusari.chainware.exception.member.InvalidMemberAuthorityException;
import com.harusari.chainware.exception.member.MemberErrorCode;
import com.harusari.chainware.franchise.command.application.service.FranchiseCommandServiceImpl;
import com.harusari.chainware.member.command.application.dto.request.MemberCreateRequest;
import com.harusari.chainware.member.command.application.dto.request.franchise.MemberWithFranchiseRequest;
import com.harusari.chainware.member.command.application.dto.request.vendor.MemberWithVendorRequest;
import com.harusari.chainware.member.command.application.dto.request.warehouse.MemberWithWarehouseRequest;
import com.harusari.chainware.member.command.domain.aggregate.Authority;
import com.harusari.chainware.member.command.domain.aggregate.Member;
import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import com.harusari.chainware.member.command.domain.repository.AuthorityCommandRepository;
import com.harusari.chainware.member.command.domain.repository.MemberCommandRepository;
import com.harusari.chainware.member.common.mapper.MemberMapStruct;
import com.harusari.chainware.vendor.command.application.service.VendorCommandService;
import com.harusari.chainware.warehouse.command.domain.aggregate.Warehouse;
import com.harusari.chainware.warehouse.command.domain.repository.WarehouseRepository;
import com.harusari.chainware.warehouse.common.mapper.WarehouseMapStruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.harusari.chainware.member.common.constants.EmailValidationConstant.EMAIL_VALIDATION_PREFIX;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberMapStruct memberMapStruct;
    private final WarehouseMapStruct warehouseMapStruct;
    private final PasswordEncoder passwordEncoder;

    private final FranchiseCommandServiceImpl franchiseCommandService;
    private final VendorCommandService vendorCommandService;

    private final MemberCommandRepository memberCommandRepository;
    private final AuthorityCommandRepository authorityCommandRepository;
    private final WarehouseRepository warehouseRepository;

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void registerHeadquartersMember(MemberCreateRequest memberCreateRequest) {
        if (
                memberCreateRequest.authorityName() == MemberAuthorityType.MASTER ||
                        memberCreateRequest.authorityName() == MemberAuthorityType.GENERAL_MANAGER ||
                        memberCreateRequest.authorityName() == MemberAuthorityType.SENIOR_MANAGER
        ) {
            registerMember(memberCreateRequest);
            deleteEmailVerificationToken(memberCreateRequest.validationToken());
        } else {
            throw new InvalidMemberAuthorityException(MemberErrorCode.INVALID_MEMBER_AUTHORITY);
        }
    }

    @Override
    public void registerFranchise(MemberWithFranchiseRequest memberWithFranchiseRequest, MultipartFile agreementFile) {
        MemberCreateRequest memberCreateRequest = memberWithFranchiseRequest.memberCreateRequest();

        if (memberCreateRequest.authorityName() == MemberAuthorityType.FRANCHISE_MANAGER) {
            Member member = registerMember(memberCreateRequest);
            franchiseCommandService.createFranchiseWithAgreement(member.getMemberId(), memberWithFranchiseRequest, agreementFile);
            deleteEmailVerificationToken(memberCreateRequest.validationToken());
        } else {
            throw new InvalidMemberAuthorityException(MemberErrorCode.INVALID_MEMBER_AUTHORITY);
        }
    }

    @Override
    public void registerVendor(MemberWithVendorRequest memberWithVendorRequest, MultipartFile agreementFile) {
        MemberCreateRequest memberCreateRequest = memberWithVendorRequest.memberCreateRequest();

        if (memberCreateRequest.authorityName() == MemberAuthorityType.VENDOR_MANAGER) {
            Member member = registerMember(memberCreateRequest);
            vendorCommandService.createFranchiseWithAgreement(member.getMemberId(), memberWithVendorRequest, agreementFile);
            deleteEmailVerificationToken(memberCreateRequest.validationToken());
        } else {
            throw new InvalidMemberAuthorityException(MemberErrorCode.INVALID_MEMBER_AUTHORITY);
        }
    }

    @Override
    public void registerWarehouse(MemberWithWarehouseRequest memberWithWarehouseRequest) {
        MemberCreateRequest memberCreateRequest = memberWithWarehouseRequest.memberCreateRequest();

        if (memberCreateRequest.authorityName() == MemberAuthorityType.WAREHOUSE_MANAGER) {
            Member member = registerMember(memberCreateRequest);
            Warehouse warehouse = warehouseMapStruct.toWarehouse(memberWithWarehouseRequest.warehouseCreateRequest(), member.getMemberId());
            warehouseRepository.save(warehouse);
            deleteEmailVerificationToken(memberCreateRequest.validationToken());
        } else {
            throw new InvalidMemberAuthorityException(MemberErrorCode.INVALID_MEMBER_AUTHORITY);
        }
    }

    private Member registerMember(MemberCreateRequest memberCreateRequest) {
        validateEmailVerification(memberCreateRequest.email(), memberCreateRequest.validationToken());

        if (memberCommandRepository.existsByEmail(memberCreateRequest.email())) {
            throw new EmailAlreadyExistsException(MemberErrorCode.EMAIL_ALREADY_EXISTS);
        }

        Member member = memberMapStruct.toMember(memberCreateRequest);
        member.updateEncodedPassword(passwordEncoder.encode(memberCreateRequest.password()));

        Authority authority = authorityCommandRepository.findByAuthorityName(memberCreateRequest.authorityName());
        member.updateAuthorityId(authority.getAuthorityId());

        memberCommandRepository.save(member);

        return member;
    }

    private void validateEmailVerification(String email, String token) {
        String redisKey = EMAIL_VALIDATION_PREFIX + token;
        String emailInRedis = redisTemplate.opsForValue().get(redisKey);

        if (emailInRedis == null || !emailInRedis.equals(email)) {
            throw new EmailVerificationRequiredException(MemberErrorCode.EMAIL_VERIFICATION_REQUIRED);
        }
    }

    private void deleteEmailVerificationToken(String token) {
        String redisKey = EMAIL_VALIDATION_PREFIX + token;
        redisTemplate.delete(redisKey);
    }

}