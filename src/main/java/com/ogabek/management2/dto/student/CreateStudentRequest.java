package com.ogabek.management2.dto.student;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateStudentRequest {
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[+]?[0-9]{9,15}$", message = "Invalid phone number format")
    private String phoneNumber;
    
    @NotBlank(message = "Parent phone number is required")
    @Pattern(regexp = "^[+]?[0-9]{9,15}$", message = "Invalid parent phone number format")
    private String parentPhoneNumber;
    
    @DecimalMin(value = "0.0", message = "Initial balance cannot be negative")
    private BigDecimal initialBalance = BigDecimal.ZERO;
    
    @NotNull(message = "Branch ID is required")
    private Long branchId;
    
    @NotBlank(message = "Username is required")
    private String username;
    
    @NotBlank(message = "Password is required")
    private String password;
}