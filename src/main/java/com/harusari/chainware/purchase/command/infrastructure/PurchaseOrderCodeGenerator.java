package com.harusari.chainware.purchase.command.infrastructure;

import com.harusari.chainware.purchase.command.domain.repository.PurchaseOrderRepository;
import com.harusari.chainware.requisition.command.infrastructure.CommonCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PurchaseOrderCodeGenerator {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final CommonCodeGenerator commonCodeGenerator;

    public String generate() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        long countToday = purchaseOrderRepository.countByCreatedAtBetween(startOfDay, endOfDay);
        return commonCodeGenerator.generate("PO-", startOfDay, endOfDay, countToday);
    }
}