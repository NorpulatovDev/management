package com.ogabek.management2.dto.teacher;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor
public class UpdateTeacherRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private BigDecimal salaryPercentage;
    private Long branchId;
}