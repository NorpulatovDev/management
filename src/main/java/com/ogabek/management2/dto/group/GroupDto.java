package com.ogabek.management2.dto.group;

import com.ogabek.management2.dto.branch.BranchDto;
import com.ogabek.management2.dto.schedule.ScheduleDto;
import com.ogabek.management2.dto.student.StudentDto;
import com.ogabek.management2.dto.teacher.TeacherDto;
import com.ogabek.management2.entity.GroupStatus;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GroupDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal pricePerLesson;
    private GroupStatus status;
    private BranchDto branch;
    private TeacherDto teacher;
    private List<StudentDto> students;
    private List<ScheduleDto> schedules;
}