package com.ogabek.management2.dto.expense;

import com.ogabek.management2.entity.ExpenseType;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateExpenseRequest {
    @NotBlank(message = "Description is required")
    private String description;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
    
    @NotNull(message = "Expense type is required")
    private ExpenseType type;
    
    @NotNull(message = "Branch ID is required")
    private Long branchId;
}

