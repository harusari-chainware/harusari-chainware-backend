package com.harusari.chainware.requisition.command.infrastructure;

import com.harusari.chainware.requisition.command.domain.repository.RequisitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RequisitionCodeGenerator {

    private final RequisitionRepository requisitionRepository;
    private final CommonCodeGenerator commonCodeGenerator;

    public String generate() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        long countToday = requisitionRepository.countByCreatedAtBetween(startOfDay, endOfDay);
        return commonCodeGenerator.generate("RQ-", startOfDay, endOfDay, countToday);
    }
}