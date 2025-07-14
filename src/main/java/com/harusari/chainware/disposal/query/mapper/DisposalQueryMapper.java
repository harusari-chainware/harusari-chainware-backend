package com.harusari.chainware.disposal.query.mapper;

import com.harusari.chainware.disposal.query.dto.DisposalListDto;
import com.harusari.chainware.disposal.query.dto.DisposalProductSearchResponseDto;
import com.harusari.chainware.disposal.query.dto.DisposalSearchRequestDto;
import com.harusari.chainware.disposal.query.dto.TakeBackSimpleResponseDto;
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

    List<DisposalProductSearchResponseDto> searchWarehouseProductWithStock(
            @Param("keyword") String keyword,
            @Param("memberId") Long memberId
    );
    List<DisposalProductSearchResponseDto> searchFranchiseProductWithStock(
            @Param("keyword") String keyword,
            @Param("memberId") Long memberId
    );
    List<DisposalProductSearchResponseDto> findTakeBackProducts(
            @Param("takeBackId") Long takeBackId
    );

    List<TakeBackSimpleResponseDto> searchTakeBacks(
            @Param("keyword") String keyword,
            @Param("memberId") Long memberId
    );

    List<TakeBackSimpleResponseDto> searchTakeBacksByWarehouse(
            @Param("warehouseId") Long warehouseId,
            @Param("keyword") String keyword
    );

    List<DisposalProductSearchResponseDto> searchAllProductsWithStock(
            @Param("keyword") String keyword
    );
}
