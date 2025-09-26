package com.ogabek.management2.mapper;

import com.ogabek.management2.dto.payment.PaymentHistoryDto;
import com.ogabek.management2.dto.payment.CreatePaymentRequest;
import com.ogabek.management2.dto.payment.ProcessPaymentRequest;
import com.ogabek.management2.entity.Payment;
import com.ogabek.management2.entity.Student;

public class PaymentMapper {
    
    public static PaymentHistoryDto toDto(Payment payment) {
        if (payment == null) return null;
        return PaymentHistoryDto.builder()
                .id(payment.getId())
                .amount(payment.getAmount())
                .type(payment.getType())
                .description(payment.getDescription())
                .createdAt(payment.getCreatedAt())
                .build();
    }
    
    public static Payment toEntity(CreatePaymentRequest request, Student student) {
        if (request == null) return null;
        return Payment.builder()
                .student(student)
                .amount(request.getAmount())
                .type(request.getType())
                .description(request.getDescription())
                .build();
    }
    
    public static Payment toEntity(ProcessPaymentRequest request, Student student) {
        if (request == null) return null;
        return Payment.builder()
                .student(student)
                .amount(request.getAmount())
                .type(request.getType())
                .description(request.getDescription())
                .build();
    }
}