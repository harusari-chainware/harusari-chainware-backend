package com.harusari.chainware.franchise.command.domain.aggregate;

import com.harusari.chainware.common.domain.vo.Address;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.harusari.chainware.franchise.command.domain.aggregate.FranchiseStatus.OPERATING;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[가맹점 - entity] 도메인 단위 테스트")
class FranchiseTest {

    @Test
    @DisplayName("Franchise 생성자 (@Builder)로 생성 시 필드가 정상적으로 설정되는지 검증")
    void testFranchiseBuilder() {
        // given
        Address address = Address.builder()
                .zipcode("12345")
                .addressRoad("서울시 강남구")
                .addressDetail("테헤란로 123")
                .build();

        LocalDateTime now = LocalDateTime.now().withNano(0);

        // when
        Franchise franchise = Franchise.builder()
                .memberId(1L)
                .franchiseName("가맹점")
                .franchiseContact("01012345678")
                .franchiseTaxId("1234567890")
                .franchiseAddress(address)
                .agreementFilePath("/path/to/file.pdf")
                .agreementOriginalFileName("file.pdf")
                .agreementFileSize(1024L)
                .contractStartDate(LocalDate.parse("2025-01-01"))
                .contractEndDate(LocalDate.parse("2026-01-01"))
                .modifiedAt(now)
                .build();

        // then
        assertThat(franchise.getMemberId()).isEqualTo(1L);
        assertThat(franchise.getFranchiseName()).isEqualTo("가맹점");
        assertThat(franchise.getFranchiseContact()).isEqualTo("01012345678");
        assertThat(franchise.getFranchiseTaxId()).isEqualTo("1234567890");
        assertThat(franchise.getFranchiseAddress()).isEqualTo(address);
        assertThat(franchise.getAgreementFilePath()).isEqualTo("/path/to/file.pdf");
        assertThat(franchise.getAgreementOriginalFileName()).isEqualTo("file.pdf");
        assertThat(franchise.getAgreementFileSize()).isEqualTo(1024L);
        assertThat(franchise.getContractStartDate()).isEqualTo(LocalDate.parse("2025-01-01"));
        assertThat(franchise.getContractEndDate()).isEqualTo(LocalDate.parse("2026-01-01"));
        assertThat(franchise.getCreatedAt()).isNotNull();
        assertThat(franchise.getModifiedAt()).isEqualTo(now);
        assertThat(franchise.getFranchiseStatus()).isEqualTo(OPERATING);
    }

    @Test
    @DisplayName("updateFranchise 메서드가 필드를 올바르게 업데이트하는지 검증")
    void testUpdateFranchise() {
        // given
        Franchise franchise = Franchise.builder()
                .memberId(1L)
                .franchiseName("가맹점")
                .franchiseContact("01012345678")
                .franchiseTaxId("1234567890")
                .franchiseAddress(Address.builder()
                        .zipcode("11111")
                        .addressRoad("서울시 구로구")
                        .addressDetail("구로로 1")
                        .build())
                .agreementFilePath("/path/to/file.pdf")
                .agreementOriginalFileName("file.pdf")
                .agreementFileSize(1024L)
                .contractStartDate(LocalDate.parse("2025-01-01"))
                .contractEndDate(LocalDate.parse("2026-01-01"))
                .modifiedAt(LocalDateTime.now().withNano(0))
                .build();

        Address newAddress = Address.builder()
                .zipcode("54321")
                .addressRoad("서울시 강남구")
                .addressDetail("테헤란로 456")
                .build();

        // when
        franchise.updateFranchise(
                "가맹점-수정",
                "01099998888",
                "0987654321",
                OPERATING,
                newAddress
        );

        // then
        assertThat(franchise.getFranchiseName()).isEqualTo("가맹점-수정");
        assertThat(franchise.getFranchiseContact()).isEqualTo("01099998888");
        assertThat(franchise.getFranchiseTaxId()).isEqualTo("0987654321");
        assertThat(franchise.getFranchiseStatus()).isEqualTo(OPERATING);
        assertThat(franchise.getFranchiseAddress()).isEqualTo(newAddress);
        assertThat(franchise.getModifiedAt()).isNotNull();
    }

    @Test
    @DisplayName("updateAgreementInfo 메서드가 파일 관련 필드를 올바르게 업데이트하는지 검증")
    void testUpdateAgreementInfo() {
        // given
        Franchise franchise = Franchise.builder()
                .memberId(1L)
                .franchiseName("가맹점")
                .franchiseContact("01012345678")
                .franchiseTaxId("1234567890")
                .franchiseAddress(Address.builder()
                        .zipcode("11111")
                        .addressRoad("서울시 구로구")
                        .addressDetail("구로로 1")
                        .build())
                .agreementFilePath("/path/to/old_file.pdf")
                .agreementOriginalFileName("old_file.pdf")
                .agreementFileSize(512L)
                .contractStartDate(LocalDate.parse("2025-01-01"))
                .contractEndDate(LocalDate.parse("2026-01-01"))
                .modifiedAt(LocalDateTime.now().withNano(0))
                .build();

        LocalDateTime uploadTime = LocalDateTime.now().withNano(0);

        // when
        franchise.updateAgreementInfo(
                "/new/path/to/file.pdf",
                "file.pdf",
                2048L,
                uploadTime
        );

        // then
        assertThat(franchise.getAgreementFilePath()).isEqualTo("/new/path/to/file.pdf");
        assertThat(franchise.getAgreementOriginalFileName()).isEqualTo("file.pdf");
        assertThat(franchise.getAgreementFileSize()).isEqualTo(2048L);
        assertThat(franchise.getAgreementUploadedAt()).isEqualTo(uploadTime);
    }

}