package com.ogabek.management2.dto.salary;

import com.ogabek.management2.dto.auth.UserDto;
import com.ogabek.management2.dto.teacher.TeacherDto;
import com.ogabek.management2.entity.SalaryStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SalaryDto {
    private Long id;
    private TeacherDto teacher;
    private BigDecimal amount;
    private LocalDate salaryMonth;
    private int totalLessons;
    private BigDecimal percentage;
    private SalaryStatus status;
    private LocalDateTime createdAt;
    private UserDto processedBy;
}
