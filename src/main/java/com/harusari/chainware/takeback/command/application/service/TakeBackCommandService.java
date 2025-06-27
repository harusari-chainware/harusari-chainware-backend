package com.harusari.chainware.takeback.command.application.service;

import com.harusari.chainware.takeback.command.application.dto.request.TakeBackCreateRequest;
import com.harusari.chainware.takeback.command.application.dto.request.TakeBackRejectRequest;
import com.harusari.chainware.takeback.command.application.dto.response.TakeBackCommandResponse;

public interface TakeBackCommandService {
    TakeBackCommandResponse createTakeBack(TakeBackCreateRequest request);
    TakeBackCommandResponse cancelTakeBack(Long takeBackId);
    TakeBackCommandResponse collectTakeBack(Long takeBackId);
    TakeBackCommandResponse approveTakeBack(Long takeBackId);
    TakeBackCommandResponse rejectTakeBack(Long takeBackId, TakeBackRejectRequest request, Long memberId);
}
