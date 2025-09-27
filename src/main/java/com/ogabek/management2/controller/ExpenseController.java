package com.ogabek.management2.controller;

import com.ogabek.management2.dto.expense.CreateExpenseRequest;
import com.ogabek.management2.dto.expense.ExpenseDto;
import com.ogabek.management2.dto.expense.UpdateExpenseRequest;
import com.ogabek.management2.entity.ExpenseType;
import com.ogabek.management2.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@Tag(name = "Expenses", description = "Expense management operations")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Create a new expense")
    public ResponseEntity<ExpenseDto> createExpense(
            @Valid @RequestBody CreateExpenseRequest request,
            Authentication authentication) {
        ExpenseDto expense = expenseService.createExpense(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(expense);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Update expense")
    public ResponseEntity<ExpenseDto> updateExpense(
            @Parameter(description = "Expense ID") @PathVariable Long id,
            @Valid @RequestBody UpdateExpenseRequest request) {
        ExpenseDto updated = expenseService.updateExpense(id, request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get expense by ID")
    public ResponseEntity<ExpenseDto> getExpenseById(
            @Parameter(description = "Expense ID") @PathVariable Long id) {
        ExpenseDto expense = expenseService.findById(id);
        return ResponseEntity.ok(expense);
    }

    @GetMapping
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get all expenses")
    public ResponseEntity<List<ExpenseDto>> getAllExpenses() {
        List<ExpenseDto> expenses = expenseService.findAll();
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/branch/{branchId}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get expenses by branch")
    public ResponseEntity<List<ExpenseDto>> getExpensesByBranch(
            @Parameter(description = "Branch ID") @PathVariable Long branchId) {
        List<ExpenseDto> expenses = expenseService.findByBranch(branchId);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/type/{type}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get expenses by type")
    public ResponseEntity<List<ExpenseDto>> getExpensesByType(
            @Parameter(description = "Expense type") @PathVariable ExpenseType type) {
        List<ExpenseDto> expenses = expenseService.findByType(type);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/branch/{branchId}/date-range")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get expenses by branch and date range")
    public ResponseEntity<List<ExpenseDto>> getExpensesByBranchAndDateRange(
            @Parameter(description = "Branch ID") @PathVariable Long branchId,
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<ExpenseDto> expenses = expenseService.findByBranchAndDateRange(branchId, startDate, endDate);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/branch/{branchId}/total")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get total expenses by branch and date range")
    public ResponseEntity<BigDecimal> getTotalExpensesByBranch(
            @Parameter(description = "Branch ID") @PathVariable Long branchId,
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        BigDecimal total = expenseService.getTotalExpensesByBranch(branchId, startDate, endDate);
        return ResponseEntity.ok(total);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DIRECTOR')")
    @Operation(summary = "Delete expense")
    public ResponseEntity<Void> deleteExpense(
            @Parameter(description = "Expense ID") @PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}