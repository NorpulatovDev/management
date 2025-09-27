package com.ogabek.management2.service;

import com.ogabek.management2.dto.expense.CreateExpenseRequest;
import com.ogabek.management2.dto.expense.ExpenseDto;
import com.ogabek.management2.dto.expense.UpdateExpenseRequest;
import com.ogabek.management2.entity.ExpenseType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {
    ExpenseDto createExpense(CreateExpenseRequest request, String createdByUsername);
    ExpenseDto updateExpense(Long id, UpdateExpenseRequest request);
    ExpenseDto findById(Long id);
    List<ExpenseDto> findAll();
    List<ExpenseDto> findByBranch(Long branchId);
    List<ExpenseDto> findByType(ExpenseType type);
    List<ExpenseDto> findByBranchAndDateRange(Long branchId, LocalDate startDate, LocalDate endDate);
    BigDecimal getTotalExpensesByBranch(Long branchId, LocalDate startDate, LocalDate endDate);
    void deleteExpense(Long id);
}