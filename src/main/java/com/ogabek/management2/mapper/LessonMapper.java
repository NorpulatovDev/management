package com.ogabek.management2.mapper;

import com.ogabek.management2.dto.lesson.LessonDto;
import com.ogabek.management2.dto.lesson.CreateLessonRequest;
import com.ogabek.management2.dto.lesson.UpdateLessonRequest;
import com.ogabek.management2.entity.Lesson;
import com.ogabek.management2.entity.Group;
import com.ogabek.management2.entity.Teacher;

public class LessonMapper {
    
    public static LessonDto toDto(Lesson lesson) {
        if (lesson == null) return null;
        return LessonDto.builder()
                .id(lesson.getId())
                .title(lesson.getTitle())
                .description(lesson.getDescription())
                .group(GroupMapper.toDto(lesson.getGroup()))
                .lessonDate(lesson.getLessonDate())
                .startTime(lesson.getStartTime())
                .endTime(lesson.getEndTime())
                .status(lesson.getStatus())
                .charged(lesson.isCharged())
                .createdBy(TeacherMapper.toDto(lesson.getCreatedBy()))
                .build();
    }
    
    public static Lesson toEntity(CreateLessonRequest request, Group group, Teacher createdBy) {
        if (request == null) return null;
        return Lesson.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .group(group)
                .lessonDate(request.getLessonDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .createdBy(createdBy)
                .build();
    }
    
    public static void updateEntity(Lesson lesson, UpdateLessonRequest request) {
        if (request == null || lesson == null) return;
        
        if (request.getTitle() != null) {
            lesson.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            lesson.setDescription(request.getDescription());
        }
        if (request.getLessonDate() != null) {
            lesson.setLessonDate(request.getLessonDate());
        }
        if (request.getStartTime() != null) {
            lesson.setStartTime(request.getStartTime());
        }
        if (request.getEndTime() != null) {
            lesson.setEndTime(request.getEndTime());
        }
        if (request.getStatus() != null) {
            lesson.setStatus(request.getStatus());
        }
    }
}