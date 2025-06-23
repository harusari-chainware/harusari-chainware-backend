package com.harusari.chainware.statistics.query.service.salesPattern;

import java.time.LocalDate;

public interface SalesPatternQueryService {

    Object getSalesPattern(String period, Long franchiseId, LocalDate targetDate);

}