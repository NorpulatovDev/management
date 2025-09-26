package com.ogabek.management2.dto.salary;

import com.ogabek.management2.entity.SalaryStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
public class UpdateSalaryRequest {
    private BigDecimal amount;
    private int totalLessons;
    private BigDecimal percentage;
    private SalaryStatus status;
}