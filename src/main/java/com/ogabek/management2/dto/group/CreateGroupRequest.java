package com.ogabek.management2.dto.group;

import com.ogabek.management2.dto.schedule.CreateScheduleRequest;
import com.ogabek.management2.entity.GroupStatus;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateGroupRequest {
    @NotBlank(message = "Group name is required")
    @Size(max = 100, message = "Group name cannot exceed 100 characters")
    private String name;
    
    @Size(max = 100, message = "Description cannot exceed 100 characters")
    private String description;
    
    @NotNull(message = "Price per lesson is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal pricePerLesson;
    
    @NotNull(message = "Branch ID is required")
    private Long branchId;
    
    @NotNull(message = "Teacher ID is required")
    private Long teacherId;
    
    private List<Long> studentIds;
    
    private List<CreateScheduleRequest> schedules;
}



