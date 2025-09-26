package com.ogabek.management2.dto.salary;

import com.ogabek.management2.entity.SalaryStatus;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateSalaryRequest {
    @NotNull(message = "Teacher ID is required")
    private Long teacherId;
    
    @NotNull(message = "Salary month is required")
    private LocalDate salaryMonth;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
    
    @Min(value = 0, message = "Total lessons cannot be negative")
    private int totalLessons;
    
    @DecimalMin(value = "0.0", message = "Percentage cannot be negative")
    @DecimalMax(value = "100.0", message = "Percentage cannot exceed 100%")
    private BigDecimal percentage;
}

