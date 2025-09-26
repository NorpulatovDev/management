package com.ogabek.management2.dto.payment;

import com.ogabek.management2.entity.PaymentType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProcessPaymentRequest {
    @NotNull(message = "Student username is required")
    private String studentUsername;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
    
    @NotNull(message = "Payment type is required")
    private PaymentType type;
    
    private String description;
}