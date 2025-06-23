package com.harusari.chainware.requisition.command.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class CommonCodeGenerator {

    public String generate(String prefix, LocalDateTime startOfDay, LocalDateTime endOfDay, long countToday) {
        String datePart = startOfDay.toLocalDate().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String sequencePart = String.format("%03d", countToday + 1);
        return prefix + datePart + sequencePart;
    }
}