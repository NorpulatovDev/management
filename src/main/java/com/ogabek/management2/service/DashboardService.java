package com.ogabek.management2.service;

import com.ogabek.management2.dto.dashboard.DashboardStatsDto;
import java.time.LocalDate;

public interface DashboardService {
    DashboardStatsDto getOverallStatistics();
    DashboardStatsDto getBranchStatistics(Long branchId);
    DashboardStatsDto getMonthlyStatistics(LocalDate month);
    DashboardStatsDto getBranchMonthlyStatistics(Long branchId, LocalDate month);
}