package com.ogabek.management2.serviceImpl;

import com.ogabek.management2.dto.expense.CreateExpenseRequest;
import com.ogabek.management2.dto.expense.ExpenseDto;
import com.ogabek.management2.dto.expense.UpdateExpenseRequest;
import com.ogabek.management2.entity.*;
import com.ogabek.management2.exception.ResourceNotFoundException;
import com.ogabek.management2.mapper.ExpenseMapper;
import com.ogabek.management2.repository.*;
import com.ogabek.management2.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;

    @Override
    public ExpenseDto createExpense(CreateExpenseRequest request, String createdByUsername) {
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));

        User createdBy = userRepository.findByUsername(createdByUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Expense expense = ExpenseMapper.toEntity(request, branch, createdBy);
        expenseRepository.save(expense);

        return ExpenseMapper.toDto(expense);
    }

    @Override
    public ExpenseDto updateExpense(Long id, UpdateExpenseRequest request) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        ExpenseMapper.updateEntity(expense, request);
        expenseRepository.save(expense);

        return ExpenseMapper.toDto(expense);
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenseDto findById(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
        return ExpenseMapper.toDto(expense);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseDto> findAll() {
        return expenseRepository.findAll()
                .stream()
                .map(ExpenseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseDto> findByBranch(Long branchId) {
        return expenseRepository.findByBranchId(branchId)
                .stream()
                .map(ExpenseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseDto> findByType(ExpenseType type) {
        return expenseRepository.findByType(type)
                .stream()
                .map(ExpenseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseDto> findByBranchAndDateRange(Long branchId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);
        
        return expenseRepository.findByBranchIdAndCreatedAtBetween(branchId, start, end)
                .stream()
                .map(ExpenseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalExpensesByBranch(Long branchId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);
        
        return expenseRepository.getTotalExpensesByBranchAndDateRange(branchId, start, end);
    }

    @Override
    public void deleteExpense(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
        
        expenseRepository.delete(expense);
    }
}