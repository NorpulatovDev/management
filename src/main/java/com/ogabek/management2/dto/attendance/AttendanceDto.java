package com.ogabek.management2.dto.attendance;

import com.ogabek.management2.dto.student.StudentDto;
import com.ogabek.management2.entity.AttendanceStatus;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AttendanceDto {
    private Long id;
    private Long lessonId;
    private StudentDto student;
    private AttendanceStatus status;
    private LocalDate date;
}