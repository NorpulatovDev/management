package com.ogabek.management2.serviceImpl;

import com.ogabek.management2.dto.dashboard.DashboardStatsDto;
import com.ogabek.management2.entity.LessonStatus;
import com.ogabek.management2.repository.*;
import com.ogabek.management2.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final GroupRepository groupRepository;
    private final LessonRepository lessonRepository;
    private final PaymentRepository paymentRepository;
    private final ExpenseRepository expenseRepository;

    @Override
    public DashboardStatsDto getOverallStatistics() {
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atTime(23, 59, 59);

        int totalStudents = (int) studentRepository.count();
        int totalTeachers = (int) teacherRepository.count();
        int totalGroups = (int) groupRepository.count();
        int activeLessons = lessonRepository.countByStatus(LessonStatus.PLANNED).intValue();

        BigDecimal totalRevenue = paymentRepository.getTotalPaymentsByDateRange(startOfMonth, endOfMonth);
        BigDecimal totalExpenses = expenseRepository.getTotalExpensesByDateRange(startOfMonth, endOfMonth);
        BigDecimal netProfit = totalRevenue.subtract(totalExpenses);

        return DashboardStatsDto.builder()
                .totalStudents(totalStudents)
                .totalTeachers(totalTeachers)
                .totalGroups(totalGroups)
                .activeLessons(activeLessons)
                .totalRevenue(totalRevenue)
                .totalExpenses(totalExpenses)
                .netProfit(netProfit)
                .build();
    }

    @Override
    public DashboardStatsDto getBranchStatistics(Long branchId) {
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atTime(23, 59, 59);

        int totalStudents = studentRepository.countByBranchId(branchId).intValue();
        int totalTeachers = teacherRepository.countByBranchId(branchId).intValue();
        int totalGroups = groupRepository.countByBranchId(branchId).intValue();
        int activeLessons = lessonRepository.countByBranchIdAndStatus(branchId, LessonStatus.PLANNED).intValue();

        BigDecimal totalRevenue = paymentRepository.getTotalPaymentsByBranchAndDateRange(branchId, startOfMonth, endOfMonth);
        BigDecimal totalExpenses = expenseRepository.getTotalExpensesByBranchAndDateRange(branchId, startOfMonth, endOfMonth);
        BigDecimal netProfit = totalRevenue.subtract(totalExpenses);

        return DashboardStatsDto.builder()
                .totalStudents(totalStudents)
                .totalTeachers(totalTeachers)
                .totalGroups(totalGroups)
                .activeLessons(activeLessons)
                .totalRevenue(totalRevenue)
                .totalExpenses(totalExpenses)
                .netProfit(netProfit)
                .build();
    }

    @Override
    public DashboardStatsDto getMonthlyStatistics(LocalDate month) {
        LocalDateTime startOfMonth = month.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = month.withDayOfMonth(month.lengthOfMonth()).atTime(23, 59, 59);

        int totalStudents = (int) studentRepository.count();
        int totalTeachers = (int) teacherRepository.count();
        int totalGroups = (int) groupRepository.count();
        int activeLessons = lessonRepository.countByDateRangeAndStatus(startOfMonth.toLocalDate(), endOfMonth.toLocalDate(), LessonStatus.PLANNED).intValue();

        BigDecimal totalRevenue = paymentRepository.getTotalPaymentsByDateRange(startOfMonth, endOfMonth);
        BigDecimal totalExpenses = expenseRepository.getTotalExpensesByDateRange(startOfMonth, endOfMonth);
        BigDecimal netProfit = totalRevenue.subtract(totalExpenses);

        return DashboardStatsDto.builder()
                .totalStudents(totalStudents)
                .totalTeachers(totalTeachers)
                .totalGroups(totalGroups)
                .activeLessons(activeLessons)
                .totalRevenue(totalRevenue)
                .totalExpenses(totalExpenses)
                .netProfit(netProfit)
                .build();
    }

    @Override
    public DashboardStatsDto getBranchMonthlyStatistics(Long branchId, LocalDate month) {
        LocalDateTime startOfMonth = month.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = month.withDayOfMonth(month.lengthOfMonth()).atTime(23, 59, 59);

        int totalStudents = studentRepository.countByBranchId(branchId).intValue();
        int totalTeachers = teacherRepository.countByBranchId(branchId).intValue();
        int totalGroups = groupRepository.countByBranchId(branchId).intValue();
        int activeLessons = lessonRepository.countByBranchIdAndDateRangeAndStatus(
            branchId, startOfMonth.toLocalDate(), endOfMonth.toLocalDate(), LessonStatus.PLANNED).intValue();

        BigDecimal totalRevenue = paymentRepository.getTotalPaymentsByBranchAndDateRange(branchId, startOfMonth, endOfMonth);
        BigDecimal totalExpenses = expenseRepository.getTotalExpensesByBranchAndDateRange(branchId, startOfMonth, endOfMonth);
        BigDecimal netProfit = totalRevenue.subtract(totalExpenses);

        return DashboardStatsDto.builder()
                .totalStudents(totalStudents)
                .totalTeachers(totalTeachers)
                .totalGroups(totalGroups)
                .activeLessons(activeLessons)
                .totalRevenue(totalRevenue)
                .totalExpenses(totalExpenses)
                .netProfit(netProfit)
                .build();
    }
}