package com.ogabek.management2.dto.teacher;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateTeacherRequest {
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    @Pattern(regexp = "^[+]?[0-9]{9,15}$", message = "Invalid phone number format")
    private String phoneNumber;
    
    @Email(message = "Invalid email format")
    private String email;
    
    @DecimalMin(value = "0.0", message = "Salary percentage cannot be negative")
    @DecimalMax(value = "100.0", message = "Salary percentage cannot exceed 100%")
    private BigDecimal salaryPercentage = BigDecimal.ZERO;
    
    @NotNull(message = "Branch ID is required")
    private Long branchId;
    
    @NotBlank(message = "Username is required")
    private String username;
    
    @NotBlank(message = "Password is required")
    private String password;
}

