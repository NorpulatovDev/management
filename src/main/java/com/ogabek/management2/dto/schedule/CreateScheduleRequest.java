package com.ogabek.management2.dto.schedule;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateScheduleRequest {
    @NotNull(message = "Day of week is required")
    private DayOfWeek dayOfWeek;
    
    @NotNull(message = "Start time is required")
    private LocalTime startTime;
    
    @NotNull(message = "End time is required")
    private LocalTime endTime;
}

