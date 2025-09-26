package com.ogabek.management2.dto.payment;

import com.ogabek.management2.entity.PaymentType;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreatePaymentRequest {
    @NotNull(message = "Student ID is required")
    private Long studentId;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
    
    @NotNull(message = "Payment type is required")
    private PaymentType type;
    
    private String description;
}

