package com.ogabek.management2.controller;

import com.ogabek.management2.dto.payment.*;
import com.ogabek.management2.service.PaymentService;
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
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Payment management operations")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/process")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DIRECTOR')")
    @Operation(summary = "Process payment for student")
    public ResponseEntity<PaymentHistoryDto> processPayment(
            @Valid @RequestBody ProcessPaymentRequest request) {
        PaymentHistoryDto payment = paymentService.processPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('DIRECTOR')")
    @Operation(summary = "Create payment record")
    public ResponseEntity<PaymentHistoryDto> createPayment(
            @Valid @RequestBody CreatePaymentRequest request) {
        PaymentHistoryDto payment = paymentService.createPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }

    @PostMapping("/charge-lesson/{lessonId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN') or hasRole('DIRECTOR')")
    @Operation(summary = "Charge lesson fee to students")
    public ResponseEntity<Void> chargeLessonFee(
            @Parameter(description = "Lesson ID") @PathVariable Long lessonId) {
        paymentService.chargeLessonFee(lessonId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/history")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Get payment history for current student")
    public ResponseEntity<List<PaymentHistoryDto>> getMyPaymentHistory(Authentication authentication) {
        List<PaymentHistoryDto> history = paymentService.getPaymentHistory(authentication.getName());
        return ResponseEntity.ok(history);
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DIRECTOR')")
    @Operation(summary = "Get payments by student ID")
    public ResponseEntity<List<PaymentHistoryDto>> getPaymentsByStudent(
            @Parameter(description = "Student ID") @PathVariable Long studentId) {
        List<PaymentHistoryDto> payments = paymentService.getPaymentsByStudent(studentId);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/balance")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Get current student balance")
    public ResponseEntity<BigDecimal> getMyBalance(Authentication authentication) {
        BigDecimal balance = paymentService.getStudentBalance(authentication.getName());
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/student/{username}/balance")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DIRECTOR')")
    @Operation(summary = "Get student balance by username")
    public ResponseEntity<BigDecimal> getStudentBalance(
            @Parameter(description = "Student username") @PathVariable String username) {
        BigDecimal balance = paymentService.getStudentBalance(username);
        return ResponseEntity.ok(balance);
    }
}