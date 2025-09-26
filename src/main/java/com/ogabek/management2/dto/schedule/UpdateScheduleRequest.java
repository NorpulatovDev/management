package com.ogabek.management2.dto.schedule;

import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor
public class UpdateScheduleRequest {
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean active;
}