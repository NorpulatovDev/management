package com.ogabek.management2.controller;

import com.ogabek.management2.dto.payment.PaymentHistoryDto;
import com.ogabek.management2.dto.student.*;
import com.ogabek.management2.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Tag(name = "Students", description = "Student management operations")
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Create a new student")
    public ResponseEntity<StudentDto> createStudent(@Valid @RequestBody CreateStudentRequest request) {
        StudentDto student = studentService.createStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(student);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Update student information")
    public ResponseEntity<StudentDto> updateStudent(
            @Parameter(description = "Student ID") @PathVariable Long id,
            @Valid @RequestBody UpdateStudentRequest request) {
        StudentDto updated = studentService.updateStudent(id, request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN') or hasRole('TEACHER')")
    @Operation(summary = "Get student by ID")
    public ResponseEntity<StudentDto> getStudentById(
            @Parameter(description = "Student ID") @PathVariable Long id) {
        StudentDto student = studentService.findById(id);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Get current student profile")
    public ResponseEntity<StudentDto> getMyProfile(Authentication authentication) {
        StudentDto student = studentService.findByUsername(authentication.getName());
        return ResponseEntity.ok(student);
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get student by username")
    public ResponseEntity<StudentDto> getStudentByUsername(
            @Parameter(description = "Username") @PathVariable String username) {
        StudentDto student = studentService.findByUsername(username);
        return ResponseEntity.ok(student);
    }

    @GetMapping
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get all students")
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        List<StudentDto> students = studentService.findAll();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/branch/{branchId}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get students by branch")
    public ResponseEntity<List<StudentDto>> getStudentsByBranch(
            @Parameter(description = "Branch ID") @PathVariable Long branchId) {
        List<StudentDto> students = studentService.findByBranch(branchId);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/group/{groupId}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN') or hasRole('TEACHER')")
    @Operation(summary = "Get students by group")
    public ResponseEntity<List<StudentDto>> getStudentsByGroup(
            @Parameter(description = "Group ID") @PathVariable Long groupId) {
        List<StudentDto> students = studentService.findByGroup(groupId);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/balance")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Get current student balance")
    public ResponseEntity<BigDecimal> getMyBalance(Authentication authentication) {
        BigDecimal balance = studentService.getBalance(authentication.getName());
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/{username}/balance")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get student balance by username")
    public ResponseEntity<BigDecimal> getStudentBalance(
            @Parameter(description = "Username") @PathVariable String username) {
        BigDecimal balance = studentService.getBalance(username);
        return ResponseEntity.ok(balance);
    }

    @PostMapping("/add-balance")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Add balance to current student account")
    public ResponseEntity<StudentDto> addBalance(
            @Valid @RequestBody AddBalanceRequest request,
            Authentication authentication) {
        StudentDto updated = studentService.addBalance(authentication.getName(), request);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{username}/add-balance")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Add balance to student account by username")
    public ResponseEntity<StudentDto> addBalanceByUsername(
            @Parameter(description = "Username") @PathVariable String username,
            @Valid @RequestBody AddBalanceRequest request) {
        StudentDto updated = studentService.addBalance(username, request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/payment-history")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Get current student payment history")
    public ResponseEntity<List<PaymentHistoryDto>> getMyPaymentHistory(Authentication authentication) {
        List<PaymentHistoryDto> history = studentService.getPaymentHistory(authentication.getName());
        return ResponseEntity.ok(history);
    }

    @GetMapping("/{username}/payment-history")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get student payment history by username")
    public ResponseEntity<List<PaymentHistoryDto>> getPaymentHistoryByUsername(
            @Parameter(description = "Username") @PathVariable String username) {
        List<PaymentHistoryDto> history = studentService.getPaymentHistory(username);
        return ResponseEntity.ok(history);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DIRECTOR')")
    @Operation(summary = "Delete student")
    public ResponseEntity<Void> deleteStudent(
            @Parameter(description = "Student ID") @PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}