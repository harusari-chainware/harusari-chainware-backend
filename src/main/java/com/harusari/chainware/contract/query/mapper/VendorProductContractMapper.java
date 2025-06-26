package com.harusari.chainware.contract.query.mapper;

import com.harusari.chainware.contract.query.dto.request.VendorProductContractSearchRequest;
import com.harusari.chainware.contract.query.dto.response.VendorProductContractDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VendorProductContractMapper {
//    List<VendorProductContractDto> findAllVendorProductContracts();

    List<VendorProductContractDto> findVendorProductContracts(
            @Param("request") VendorProductContractSearchRequest request,
            @Param("isManager") boolean isManager
    );

    long countVendorProductContracts(
            @Param("request") VendorProductContractSearchRequest request,
            @Param("isManager") boolean isManager
    );

    List<VendorProductContractDto> findVendorProductContractsByVendorId(@Param("vendorId") Long vendorId);
}