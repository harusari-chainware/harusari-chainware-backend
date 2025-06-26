package com.harusari.chainware.contract.query.mapper;

import com.harusari.chainware.contract.query.dto.response.VendorProductContractDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VendorProductContractMapper {

    List<VendorProductContractDto> findAllVendorProductContracts();

    List<VendorProductContractDto> findVendorProductContractsByVendorId(@Param("vendorId") Long vendorId);
}