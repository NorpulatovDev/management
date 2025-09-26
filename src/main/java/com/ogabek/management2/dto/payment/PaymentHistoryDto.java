package com.ogabek.management2.dto.payment;

import com.ogabek.management2.entity.PaymentType;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentHistoryDto {
    private Long id;
    private BigDecimal amount;
    private PaymentType type;
    private String description;
    private LocalDateTime createdAt;
}