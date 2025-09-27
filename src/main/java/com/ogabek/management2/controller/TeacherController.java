package com.ogabek.management2.controller;

import com.ogabek.management2.dto.teacher.*;
import com.ogabek.management2.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
@Tag(name = "Teachers", description = "Teacher management operations")
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Create a new teacher")
    public ResponseEntity<TeacherDto> createTeacher(@Valid @RequestBody CreateTeacherRequest request) {
        TeacherDto teacher = teacherService.createTeacher(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(teacher);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Update teacher information")
    public ResponseEntity<TeacherDto> updateTeacher(
            @Parameter(description = "Teacher ID") @PathVariable Long id,
            @Valid @RequestBody UpdateTeacherRequest request) {
        TeacherDto updated = teacherService.updateTeacher(id, request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get teacher by ID")
    public ResponseEntity<TeacherDto> getTeacherById(
            @Parameter(description = "Teacher ID") @PathVariable Long id) {
        TeacherDto teacher = teacherService.findById(id);
        return ResponseEntity.ok(teacher);
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Get current teacher profile")
    public ResponseEntity<TeacherDto> getMyProfile(Authentication authentication) {
        TeacherDto teacher = teacherService.findByUsername(authentication.getName());
        return ResponseEntity.ok(teacher);
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get teacher by username")
    public ResponseEntity<TeacherDto> getTeacherByUsername(
            @Parameter(description = "Username") @PathVariable String username) {
        TeacherDto teacher = teacherService.findByUsername(username);
        return ResponseEntity.ok(teacher);
    }

    @GetMapping
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get all teachers")
    public ResponseEntity<List<TeacherDto>> getAllTeachers() {
        List<TeacherDto> teachers = teacherService.findAll();
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("/branch/{branchId}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get teachers by branch")
    public ResponseEntity<List<TeacherDto>> getTeachersByBranch(
            @Parameter(description = "Branch ID") @PathVariable Long branchId) {
        List<TeacherDto> teachers = teacherService.findByBranch(branchId);
        return ResponseEntity.ok(teachers);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DIRECTOR')")
    @Operation(summary = "Delete teacher")
    public ResponseEntity<Void> deleteTeacher(
            @Parameter(description = "Teacher ID") @PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }
}