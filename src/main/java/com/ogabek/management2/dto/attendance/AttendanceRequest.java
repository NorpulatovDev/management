package com.ogabek.management2.dto.attendance;

import com.ogabek.management2.entity.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AttendanceRequest {
    @NotNull(message = "Student ID is required")
    private Long studentId;
    
    @NotNull(message = "Attendance status is required")
    private AttendanceStatus status;
}

