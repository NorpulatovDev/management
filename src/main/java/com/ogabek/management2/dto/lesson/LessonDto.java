package com.ogabek.management2.dto.lesson;

import com.ogabek.management2.dto.group.GroupDto;
import com.ogabek.management2.dto.teacher.TeacherDto;
import com.ogabek.management2.entity.LessonStatus;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LessonDto {
    private Long id;
    private String title;
    private String description;
    private GroupDto group;
    private LocalDate lessonDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private LessonStatus status;
    private boolean charged;
    private TeacherDto createdBy;
}