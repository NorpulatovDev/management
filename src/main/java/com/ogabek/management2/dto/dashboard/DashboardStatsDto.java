package com.ogabek.management2.dto.dashboard;

import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class DashboardStatsDto {
    private int totalStudents;
    private int totalTeachers;
    private int totalGroups;
    private int activeLessons;
    private BigDecimal totalRevenue;
    private BigDecimal totalExpenses;
    private BigDecimal netProfit;
}