package com.ogabek.management2.dto.lesson;

import com.ogabek.management2.entity.LessonStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor
public class UpdateLessonRequest {
    private String title;
    private String description;
    private LocalDate lessonDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private LessonStatus status;
}