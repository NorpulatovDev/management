package com.ogabek.management2.dto.schedule;

import lombok.*;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ScheduleDto {
    private Long id;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean active;
}