package com.harusari.chainware.takeback.query.repository;

import com.harusari.chainware.takeback.query.dto.request.TakeBackSearchRequest;
import com.harusari.chainware.takeback.query.dto.response.TakeBackSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TakeBackQueryRepositoryCustom {
    Page<TakeBackSearchResponse> searchTakeBackList(TakeBackSearchRequest request, Pageable pageable);
}
