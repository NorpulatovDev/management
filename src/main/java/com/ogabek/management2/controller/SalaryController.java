package com.ogabek.management2.controller;

import com.ogabek.management2.dto.salary.*;
import com.ogabek.management2.service.SalaryService;
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

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/salaries")
@RequiredArgsConstructor
@Tag(name = "Salaries", description = "Salary management operations")
public class SalaryController {

    private final SalaryService salaryService;

    @PostMapping
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Create salary record")
    public ResponseEntity<SalaryDto> createSalary(
            @Valid @RequestBody CreateSalaryRequest request,
            Authentication authentication) {
        SalaryDto salary = salaryService.createSalary(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(salary);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Update salary record")
    public ResponseEntity<SalaryDto> updateSalary(
            @Parameter(description = "Salary ID") @PathVariable Long id,
            @Valid @RequestBody UpdateSalaryRequest request) {
        SalaryDto updated = salaryService.updateSalary(id, request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get salary by ID")
    public ResponseEntity<SalaryDto> getSalaryById(
            @Parameter(description = "Salary ID") @PathVariable Long id) {
        SalaryDto salary = salaryService.findById(id);
        return ResponseEntity.ok(salary);
    }

    @GetMapping("/teacher/{teacherId}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get salaries by teacher")
    public ResponseEntity<List<SalaryDto>> getSalariesByTeacher(
            @Parameter(description = "Teacher ID") @PathVariable Long teacherId) {
        List<SalaryDto> salaries = salaryService.findByTeacher(teacherId);
        return ResponseEntity.ok(salaries);
    }

    @GetMapping("/branch/{branchId}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get salaries by branch")
    public ResponseEntity<List<SalaryDto>> getSalariesByBranch(
            @Parameter(description = "Branch ID") @PathVariable Long branchId) {
        List<SalaryDto> salaries = salaryService.findByBranch(branchId);
        return ResponseEntity.ok(salaries);
    }

    @GetMapping("/month/{month}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get salaries by month")
    public ResponseEntity<List<SalaryDto>> getSalariesByMonth(
            @Parameter(description = "Month (YYYY-MM-DD format)") 
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate month) {
        List<SalaryDto> salaries = salaryService.findByMonth(month);
        return ResponseEntity.ok(salaries);
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get pending salaries")
    public ResponseEntity<List<SalaryDto>> getPendingSalaries() {
        List<SalaryDto> salaries = salaryService.findPendingSalaries();
        return ResponseEntity.ok(salaries);
    }

    @PostMapping("/calculate-monthly")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Calculate and create monthly salary for teacher")
    public ResponseEntity<SalaryDto> calculateMonthlySalary(
            @Parameter(description = "Teacher ID") @RequestParam Long teacherId,
            @Parameter(description = "Month (YYYY-MM-DD format)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate month,
            Authentication authentication) {
        SalaryDto salary = salaryService.calculateAndCreateMonthlySalary(
                teacherId, month, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(salary);
    }

    @PutMapping("/{id}/mark-paid")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Mark salary as paid")
    public ResponseEntity<SalaryDto> markSalaryAsPaid(
            @Parameter(description = "Salary ID") @PathVariable Long id,
            Authentication authentication) {
        SalaryDto updated = salaryService.markSalaryAsPaid(id, authentication.getName());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DIRECTOR')")
    @Operation(summary = "Delete salary record")
    public ResponseEntity<Void> deleteSalary(
            @Parameter(description = "Salary ID") @PathVariable Long id) {
        salaryService.deleteSalary(id);
        return ResponseEntity.noContent().build();
    }
}