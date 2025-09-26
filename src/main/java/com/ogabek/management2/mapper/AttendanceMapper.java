package com.ogabek.management2.mapper;

import com.ogabek.management2.dto.attendance.AttendanceDto;
import com.ogabek.management2.dto.attendance.AttendanceRequest;
import com.ogabek.management2.entity.Attendance;
import com.ogabek.management2.entity.Lesson;
import com.ogabek.management2.entity.Student;

public class AttendanceMapper {

    public static AttendanceDto toDto(Attendance attendance) {
        if (attendance == null) return null;
        return AttendanceDto.builder()
                .id(attendance.getId())
                .lessonId(attendance.getLesson().getId())
                .student(StudentMapper.toDto(attendance.getStudent()))
                .status(attendance.getStatus())
                .date(attendance.getDate())
                .build();
    }

    public static Attendance toEntity(AttendanceRequest request, Lesson lesson, Student student) {
        if (request == null) return null;
        return Attendance.builder()
                .lesson(lesson)
                .student(student)
                .status(request.getStatus())
                .date(lesson.getLessonDate())
                .build();
    }
}