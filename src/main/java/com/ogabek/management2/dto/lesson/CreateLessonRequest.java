package com.ogabek.management2.dto.lesson;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor @Builder
public class CreateLessonRequest {
    @NotBlank
    private String title;
    
    private String description;
    
    @NotNull
    private Long groupId;
    
    @NotNull
    private LocalDate lessonDate;
    
    @NotNull
    private LocalTime startTime;
    
    @NotNull
    private LocalTime endTime;
}