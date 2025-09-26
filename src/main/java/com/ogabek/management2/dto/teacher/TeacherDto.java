package com.ogabek.management2.dto.teacher;

import com.ogabek.management2.dto.auth.UserDto;
import com.ogabek.management2.dto.branch.BranchDto;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TeacherDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private BigDecimal salaryPercentage;
    private BranchDto branch;
    private UserDto user;
}