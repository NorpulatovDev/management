package com.ogabek.management2.controller;

import com.ogabek.management2.dto.lesson.*;
import com.ogabek.management2.entity.LessonStatus;
import com.ogabek.management2.service.LessonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
@Tag(name = "Lessons", description = "Lesson management operations")
public class LessonController {

    private final LessonService lessonService;

    @PostMapping("/generate-monthly")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Generate monthly lessons for all active groups")
    public ResponseEntity<Void> generateMonthlyLessons(
            @Valid @RequestBody GenerateMonthlyLessonsRequest request) {
        lessonService.generateMonthlyLessons(request.getMonth());
        return ResponseEntity.ok().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN') or hasRole('DIRECTOR')")
    @Operation(summary = "Create a new lesson")
    public ResponseEntity<LessonDto> createLesson(
            @Valid @RequestBody CreateLessonRequest request,
            Authentication authentication) {
        LessonDto lesson = lessonService.createLesson(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(lesson);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN') or hasRole('DIRECTOR')")
    @Operation(summary = "Update lesson")
    public ResponseEntity<LessonDto> updateLesson(
            @Parameter(description = "Lesson ID") @PathVariable Long id,
            @Valid @RequestBody UpdateLessonRequest request) {
        LessonDto updated = lessonService.updateLesson(id, request);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN') or hasRole('DIRECTOR')")
    @Operation(summary = "Update lesson status")
    public ResponseEntity<LessonDto> updateLessonStatus(
            @Parameter(description = "Lesson ID") @PathVariable Long id,
            @Parameter(description = "New status") @RequestParam LessonStatus status,
            Authentication authentication) {
        LessonDto updated = lessonService.updateLessonStatus(id, status, authentication.getName());
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN') or hasRole('DIRECTOR')")
    @Operation(summary = "Get lesson by ID")
    public ResponseEntity<LessonDto> getLessonById(
            @Parameter(description = "Lesson ID") @PathVariable Long id) {
        LessonDto lesson = lessonService.findById(id);
        return ResponseEntity.ok(lesson);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('DIRECTOR')")
    @Operation(summary = "Get all lessons")
    public ResponseEntity<List<LessonDto>> getAllLessons() {
        List<LessonDto> lessons = lessonService.findAll();
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/group/{groupId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN') or hasRole('DIRECTOR')")
    @Operation(summary = "Get lessons by group")
    public ResponseEntity<List<LessonDto>> getLessonsByGroup(
            @Parameter(description = "Group ID") @PathVariable Long groupId) {
        List<LessonDto> lessons = lessonService.findByGroup(groupId);
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/my-lessons")
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Get lessons for current teacher")
    public ResponseEntity<List<LessonDto>> getMyLessons(
            Authentication authentication,
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<LessonDto> lessons = lessonService.findByTeacher(authentication.getName(), startDate, endDate);
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/branch/{branchId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DIRECTOR')")
    @Operation(summary = "Get lessons by branch")
    public ResponseEntity<List<LessonDto>> getLessonsByBranch(
            @Parameter(description = "Branch ID") @PathVariable Long branchId) {
        List<LessonDto> lessons = lessonService.findByBranch(branchId);
        return ResponseEntity.ok(lessons);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN') or hasRole('DIRECTOR')")
    @Operation(summary = "Delete lesson")
    public ResponseEntity<Void> deleteLesson(
            @Parameter(description = "Lesson ID") @PathVariable Long id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }
}