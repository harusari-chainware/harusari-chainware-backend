package com.harusari.chainware.disposal.query.mapper;

import com.harusari.chainware.disposal.query.dto.DisposalListDto;
import com.harusari.chainware.disposal.query.dto.DisposalSearchRequestDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DisposalQueryMapper {
    List<DisposalListDto> findDisposals(
            @Param("request") DisposalSearchRequestDto request,
            @Param("franchiseId") Long franchiseId,
            @Param("warehouseId") Long warehouseId
    );

    long countDisposals(
            @Param("request") DisposalSearchRequestDto request,
            @Param("franchiseId") Long franchiseId,
            @Param("warehouseId") Long warehouseId
    );

}
