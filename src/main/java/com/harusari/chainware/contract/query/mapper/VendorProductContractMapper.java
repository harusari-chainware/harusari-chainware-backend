package com.harusari.chainware.contract.query.mapper;

import com.harusari.chainware.contract.query.dto.request.VendorProductContractSearchRequest;
import com.harusari.chainware.contract.query.dto.response.VendorProductContractDto;
import com.harusari.chainware.contract.query.dto.response.VendorProductContractListDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface VendorProductContractMapper {
    List<VendorProductContractListDto> findVendorProductContracts(
            @Param("request") VendorProductContractSearchRequest request,
            @Param("vendorId") Long vendorId,
            @Param("isManager") boolean isManager
    );

    long countVendorProductContracts(
            @Param("request") VendorProductContractSearchRequest request,
            @Param("vendorId") Long vendorId,
            @Param("isManager") boolean isManager
    );

    Optional<Long> findVendorIdByMemberId(@Param("memberId") Long memberId);

    Optional<VendorProductContractDto> findVendorProductContractById(
            @Param("contractId") Long contractId,
            @Param("vendorId")    Long vendorId,
            @Param("isManager")   boolean isManager
    );
}
