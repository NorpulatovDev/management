package com.ogabek.management2.service;

import com.ogabek.management2.dto.payment.CreatePaymentRequest;
import com.ogabek.management2.dto.payment.PaymentHistoryDto;
import com.ogabek.management2.dto.payment.ProcessPaymentRequest;
import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {
    PaymentHistoryDto processPayment(ProcessPaymentRequest request);
    PaymentHistoryDto createPayment(CreatePaymentRequest request);
    void chargeLessonFee(Long lessonId);
    List<PaymentHistoryDto> getPaymentHistory(String studentUsername);
    List<PaymentHistoryDto> getPaymentsByStudent(Long studentId);
//    List<PaymentHistoryDto> getPaymentsByBranch(Long branchId, LocalDate startDate, LocalDate endDate);
//    BigDecimal getTotalPaymentsByBranch(Long branchId, LocalDate startDate, LocalDate endDate);
    BigDecimal getStudentBalance(String studentUsername);
}