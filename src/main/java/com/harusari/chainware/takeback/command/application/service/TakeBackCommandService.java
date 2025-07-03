package com.harusari.chainware.takeback.command.application.service;

import com.harusari.chainware.takeback.command.application.dto.request.TakeBackCreateRequest;
import com.harusari.chainware.takeback.command.application.dto.request.TakeBackRejectRequest;
import com.harusari.chainware.takeback.command.application.dto.response.TakeBackCommandResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TakeBackCommandService {
    TakeBackCommandResponse createTakeBack(TakeBackCreateRequest request, List<MultipartFile> imageFiles, Long memberId);
    TakeBackCommandResponse cancelTakeBack(Long takeBackId, Long memberId);
    TakeBackCommandResponse collectTakeBack(Long takeBackId, Long memberId);
    TakeBackCommandResponse approveTakeBack(Long takeBackId, Long memberId);
    TakeBackCommandResponse rejectTakeBack(Long takeBackId, TakeBackRejectRequest request, Long memberId);
}
