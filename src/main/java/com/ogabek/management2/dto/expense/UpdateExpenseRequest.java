package com.ogabek.management2.dto.expense;

import com.ogabek.management2.entity.ExpenseType;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UpdateExpenseRequest {
    private String description;
    private BigDecimal amount;
    private ExpenseType type;
}