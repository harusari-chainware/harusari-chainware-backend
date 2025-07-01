package com.harusari.chainware.takeback.query.service;

import com.harusari.chainware.common.dto.PageResponse;
import com.harusari.chainware.takeback.query.dto.request.TakeBackSearchRequest;
import com.harusari.chainware.takeback.query.dto.response.TakeBackDetailResponse;
import com.harusari.chainware.takeback.query.dto.response.TakeBackSearchResponse;
import org.springframework.data.domain.Pageable;

public interface TakeBackQueryService {
    PageResponse<TakeBackSearchResponse> getTakeBackList(TakeBackSearchRequest request, Pageable pageable);
    TakeBackDetailResponse getTakeBackDetail(Long takeBackId);
}
