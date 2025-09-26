package com.ogabek.management2.dto.expense;

import com.ogabek.management2.dto.auth.UserDto;
import com.ogabek.management2.dto.branch.BranchDto;
import com.ogabek.management2.entity.ExpenseType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ExpenseDto {
    private Long id;
    private String description;
    private BigDecimal amount;
    private ExpenseType type;
    private BranchDto branch;
    private UserDto createdBy;
    private LocalDateTime createdAt;
}