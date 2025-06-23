package com.harusari.chainware.statistics.query.service;

import java.time.LocalDate;

public interface SalesPatternQueryService {

    Object getSalesPattern(String period, Long franchiseId, LocalDate targetDate);

}