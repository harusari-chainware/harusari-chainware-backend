package com.harusari.chainware.takeback.query.service;

import com.harusari.chainware.common.dto.PageResponse;
import com.harusari.chainware.takeback.query.dto.request.TakeBackSearchRequest;
import com.harusari.chainware.takeback.query.dto.response.TakeBackDetailResponse;
import com.harusari.chainware.takeback.query.dto.response.TakeBackSearchResponse;
import com.harusari.chainware.takeback.query.repository.TakeBackQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TakeBackQueryServiceImpl implements TakeBackQueryService {

    private final TakeBackQueryRepository takeBackQueryRepository;

    // 반품 목록 조회
    @Override
    public PageResponse<TakeBackSearchResponse> getTakeBackList(TakeBackSearchRequest request, Pageable pageable) {
        return PageResponse.from(takeBackQueryRepository.searchTakeBackList(request, pageable));
    }

    // 반품 상세 조회
    @Override
    public TakeBackDetailResponse getTakeBackDetail(Long takeBackId) {
        return takeBackQueryRepository.findTakeBackDetailById(takeBackId);
    }

}
