package com.ogabek.management2.dto.lesson;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class GenerateMonthlyLessonsRequest {
    @NotNull(message = "Month is required")
    private LocalDate month;
    
    private Long branchId; // Optional: generate for specific branch only
    private Long groupId;  // Optional: generate for specific group only
}