package com.harusari.chainware.requisition.command.infrastructure;

import com.harusari.chainware.requisition.command.domain.repository.RequisitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class RequisitionCodeGenerator {

    private final RequisitionRepository requisitionRepository;

    public String generate() {
        LocalDate today = LocalDate.now();
        String datePart = today.format(DateTimeFormatter.ofPattern("yyMMdd"));

        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        long countToday = requisitionRepository.countByCreatedAtBetween(startOfDay, endOfDay);

        // 1부터 시작해서 3자리로 채움 → 001, 002, ...
        String sequencePart = String.format("%03d", countToday + 1);

        return "RQ-" + datePart + sequencePart;
    }
}
