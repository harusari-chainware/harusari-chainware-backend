package com.harusari.chainware.franchise.command.application.service;

import com.harusari.chainware.common.domain.vo.Address;
import com.harusari.chainware.common.dto.AddressRequest;
import com.harusari.chainware.common.infrastructure.storage.StorageUploader;
import com.harusari.chainware.common.mapstruct.AddressMapStruct;
import com.harusari.chainware.exception.franchise.FranchiseNotFoundException;
import com.harusari.chainware.franchise.command.application.dto.request.UpdateFranchiseRequest;
import com.harusari.chainware.franchise.command.domain.aggregate.Franchise;
import com.harusari.chainware.franchise.command.domain.repository.FranchiseRepository;
import com.harusari.chainware.franchise.common.mapstruct.FranchiseMapStruct;
import com.harusari.chainware.member.command.application.dto.request.franchise.FranchiseCreateRequest;
import com.harusari.chainware.member.command.application.dto.request.franchise.MemberWithFranchiseRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Optional;

import static com.harusari.chainware.exception.franchise.FranchiseErrorCode.FRANCHISE_NOT_FOUND_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("[가맹점 - service] FranchiseCommandServiceImpl 테스트")
class FranchiseCommandServiceImplTest {

    @Spy
    private FranchiseMapStruct franchiseMapStruct = Mappers.getMapper(FranchiseMapStruct.class);

    @Spy
    private AddressMapStruct addressMapStruct = Mappers.getMapper(AddressMapStruct.class);

    @Mock
    private FranchiseRepository franchiseRepository;

    @Mock
    private StorageUploader s3Uploader;

    @InjectMocks
    private FranchiseCommandServiceImpl franchiseCommandService;

    private Franchise franchise;

    @BeforeEach
    void setUp() {
        Address address = Address.builder()
                .zipcode("12345")
                .addressRoad("서울시 OOO 123")
                .addressDetail("OOO빌딩 3층")
                .build();

        franchise = Franchise.builder()
                .memberId(1L)
                .franchiseName("가맹점01")
                .franchiseContact("01012345678")
                .franchiseTaxId("0123456789")
                .franchiseAddress(address)
                .agreementFilePath("franchises/contract.pdf")
                .agreementOriginalFileName("contract.pdf")
                .agreementFileSize(1024L)
                .contractStartDate(LocalDate.parse("2025-06-30"))
                .contractEndDate(LocalDate.parse("2027-07-01"))
                .build();
    }

    private MultipartFile createMockAgreementFile(String filename, long size) {
        MultipartFile mockFile = mock(MultipartFile.class);
        lenient().when(mockFile.getOriginalFilename()).thenReturn(filename);
        lenient().when(mockFile.getSize()).thenReturn(size);
        lenient().when(mockFile.isEmpty()).thenReturn(false);
        return mockFile;
    }

    @Test
    @DisplayName("[가맹점 등록] 가맹점 등록 시 정상 동작 테스트")
    void testCreateFranchiseWithAgreement() {
        Long memberId = 1L;
        String expectedFilePath = "franchises/contract.pdf";
        MultipartFile mockAgreementFile = createMockAgreementFile("contract.pdf", 1024L);

        AddressRequest addressRequest = AddressRequest.builder()
                .zipcode("12345")
                .addressRoad("서울시 OOO 123")
                .addressDetail("OOO빌딩 3층")
                .build();

        FranchiseCreateRequest franchiseCreateRequest = FranchiseCreateRequest.builder()
                .franchiseName("가맹점01")
                .franchiseContact("01012345678")
                .franchiseTaxId("0123456789")
                .addressRequest(addressRequest)
                .contractStartDate(LocalDate.parse("2025-06-30"))
                .contractEndDate(LocalDate.parse("2027-07-01"))
                .build();

        MemberWithFranchiseRequest memberWithFranchiseRequest = MemberWithFranchiseRequest.builder()
                .memberCreateRequest(null)
                .franchiseCreateRequest(franchiseCreateRequest)
                .build();

        when(s3Uploader.uploadAgreement(mockAgreementFile, "franchises")).thenReturn(expectedFilePath);
        when(franchiseRepository.save(any(Franchise.class))).thenAnswer(invocation -> invocation.getArgument(0));

        franchiseCommandService.createFranchiseWithAgreement(memberId, memberWithFranchiseRequest, mockAgreementFile);

        verify(s3Uploader).uploadAgreement(mockAgreementFile, "franchises");
        verify(franchiseRepository).save(any(Franchise.class));

        ArgumentCaptor<Franchise> captor = ArgumentCaptor.forClass(Franchise.class);
        verify(franchiseRepository).save(captor.capture());
        Franchise savedFranchise = captor.getValue();

        assertThat(savedFranchise.getFranchiseName()).isEqualTo("가맹점01");
        assertThat(savedFranchise.getFranchiseContact()).isEqualTo("01012345678");
        assertThat(savedFranchise.getFranchiseTaxId()).isEqualTo("0123456789");
        assertThat(savedFranchise.getFranchiseAddress().getZipcode()).isEqualTo("12345");
        assertThat(savedFranchise.getFranchiseAddress().getAddressRoad()).isEqualTo("서울시 OOO 123");
        assertThat(savedFranchise.getFranchiseAddress().getAddressDetail()).isEqualTo("OOO빌딩 3층");
        assertThat(savedFranchise.getAgreementFilePath()).isEqualTo(expectedFilePath);
        assertThat(savedFranchise.getAgreementOriginalFileName()).isEqualTo("contract.pdf");
        assertThat(savedFranchise.getAgreementFileSize()).isEqualTo(1024L);
        assertThat(savedFranchise.getAgreementUploadedAt()).isNotNull();
    }

    @Test
    @DisplayName("[가맹점 수정] 가맹점 정보 수정 시 정상 동작 테스트")
    void testUpdateFranchise() {
        Long franchiseId = 1L;
        String expectedFilePath = "franchises/contract-update.pdf";
        MultipartFile mockAgreementFile = createMockAgreementFile("contract-update.pdf", 2048L);

        AddressRequest updateAddressRequest = AddressRequest.builder()
                .zipcode("54321")
                .addressRoad("서울시 수정로 456")
                .addressDetail("수정빌딩 5층")
                .build();

        UpdateFranchiseRequest updateRequest = UpdateFranchiseRequest.builder()
                .franchiseName("가맹점01-수정")
                .franchiseContact("01099999999")
                .franchiseTaxId("9876543210")
                .franchiseStatus(franchise.getFranchiseStatus())
                .addressRequest(updateAddressRequest)
                .contractStartDate(LocalDate.parse("2025-06-30"))
                .contractEndDate(LocalDate.parse("2030-07-07"))
                .build();

        when(franchiseRepository.findFranchiseByFranchiseId(franchiseId)).thenReturn(Optional.of(franchise));
        when(s3Uploader.uploadAgreement(mockAgreementFile, "franchises")).thenReturn(expectedFilePath);

        franchiseCommandService.updateFranchise(franchiseId, updateRequest, mockAgreementFile);

        verify(franchiseRepository).findFranchiseByFranchiseId(franchiseId);
        verify(s3Uploader).uploadAgreement(mockAgreementFile, "franchises");

        assertThat(franchise.getFranchiseName()).isEqualTo("가맹점01-수정");
        assertThat(franchise.getFranchiseContact()).isEqualTo("01099999999");
        assertThat(franchise.getFranchiseTaxId()).isEqualTo("9876543210");
        assertThat(franchise.getFranchiseAddress().getZipcode()).isEqualTo("54321");
        assertThat(franchise.getFranchiseAddress().getAddressRoad()).isEqualTo("서울시 수정로 456");
        assertThat(franchise.getFranchiseAddress().getAddressDetail()).isEqualTo("수정빌딩 5층");
        assertThat(franchise.getAgreementFilePath()).isEqualTo(expectedFilePath);
        assertThat(franchise.getAgreementOriginalFileName()).isEqualTo("contract-update.pdf");
        assertThat(franchise.getAgreementFileSize()).isEqualTo(2048L);
        assertThat(franchise.getAgreementUploadedAt()).isNotNull();
        assertThat(franchise.getContractEndDate()).isEqualTo("2030-07-07");
    }

    @Test
    @DisplayName("[가맹점 수정] 가맹점 ID가 존재하지 않아 FranchiseNotFoundException이 발생하는 테스트")
    void testUpdateFranchise_NotFound() {
        Long invalidFranchiseId = 999L;

        UpdateFranchiseRequest updateRequest = UpdateFranchiseRequest.builder()
                .franchiseName("가맹점-수정")
                .franchiseContact("01099999999")
                .franchiseTaxId("9876543210")
                .franchiseStatus(null)
                .addressRequest(null)
                .build();

        MultipartFile mockAgreementFile = mock(MultipartFile.class);

        when(franchiseRepository.findFranchiseByFranchiseId(invalidFranchiseId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> franchiseCommandService.updateFranchise(invalidFranchiseId, updateRequest, mockAgreementFile))
                .isInstanceOf(FranchiseNotFoundException.class)
                .hasMessage(FRANCHISE_NOT_FOUND_EXCEPTION.getErrorMessage());

        verify(franchiseRepository).findFranchiseByFranchiseId(invalidFranchiseId);
        verifyNoInteractions(s3Uploader);
    }

}