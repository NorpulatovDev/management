package com.ogabek.management2.dto.attendance;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MarkAttendanceRequest {
    @NotNull(message = "Lesson ID is required")
    private Long lessonId;
    
    @NotNull(message = "Attendance list is required")
    @NotEmpty(message = "At least one attendance record is required")
    private List<AttendanceRequest> attendances;
}