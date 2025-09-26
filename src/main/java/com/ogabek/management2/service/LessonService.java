package com.ogabek.management2.service;

import com.ogabek.management2.dto.*;
import com.ogabek.management2.dto.lesson.CreateLessonRequest;
import com.ogabek.management2.dto.lesson.LessonDto;
import com.ogabek.management2.dto.lesson.UpdateLessonRequest;
import com.ogabek.management2.entity.LessonStatus;
import java.time.LocalDate;
import java.util.List;

public interface LessonService {
    void generateMonthlyLessons(LocalDate month);
    LessonDto createLesson(CreateLessonRequest request, String teacherUsername);
    LessonDto updateLesson(Long id, UpdateLessonRequest request);
    LessonDto updateLessonStatus(Long lessonId, LessonStatus status, String username);
    LessonDto findById(Long id);
    List<LessonDto> findAll();
    List<LessonDto> findByGroup(Long groupId);
    List<LessonDto> findByTeacher(String teacherUsername, LocalDate startDate, LocalDate endDate);
    List<LessonDto> findByBranch(Long branchId);
    void deleteLesson(Long id);
}