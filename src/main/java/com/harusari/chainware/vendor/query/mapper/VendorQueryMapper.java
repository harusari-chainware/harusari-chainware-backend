package com.harusari.chainware.vendor.query.mapper;

import com.harusari.chainware.vendor.query.dto.VendorDetailDto;
import com.harusari.chainware.vendor.query.dto.VendorListDto;
import com.harusari.chainware.vendor.query.dto.VendorSearchRequestDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface VendorQueryMapper {

    List<VendorListDto> findVendors (VendorSearchRequestDto request);

    long countVendors(VendorSearchRequestDto request);

    VendorDetailDto findVendorDetailById(Long vendorId);

}
