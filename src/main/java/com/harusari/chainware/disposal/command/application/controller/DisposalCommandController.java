package com.harusari.chainware.disposal.command.application.controller;

import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.disposal.command.application.dto.DisposalCommandRequestDto;
import com.harusari.chainware.disposal.command.application.service.DisposalCommandService;
import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/disposal")
@RequiredArgsConstructor
@Tag(name = "폐기 등록 API", description = "창고 또는 가맹점의 폐기 데이터를 등록하는 API입니다.")
public class DisposalCommandController {

    private final DisposalCommandService disposalCommandService;

    @Operation(
            summary = "폐기 등록",
            description = """
        창고 또는 가맹점의 폐기 데이터를 등록합니다.  
        - 인증된 사용자의 권한(FRANCHISE_MANAGER 또는 WAREHOUSE_MANAGER)에 따라 폐기 대상이 결정됩니다.  
        - 등록 요청에는 상품 ID, 수량, 사유, 수거 ID(선택)를 포함해야 합니다.
        """
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "폐기 등록 성공")
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> registerDisposal(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "폐기 등록 요청 정보 (상품 ID, 수량, 사유 등 포함)")
            @RequestBody DisposalCommandRequestDto request,

            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMemberId();
        MemberAuthorityType authorityType = userDetails.getMemberAuthorityType();

        disposalCommandService.registerDisposal(request, memberId, authorityType);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
