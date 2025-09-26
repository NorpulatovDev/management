package com.ogabek.management2.dto.student;

import com.ogabek.management2.dto.auth.UserDto;
import com.ogabek.management2.dto.branch.BranchDto;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StudentDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String parentPhoneNumber;
    private BigDecimal balance;
    private BranchDto branch;
    private UserDto user;
}