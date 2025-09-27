package com.ogabek.management2.controller;

import com.ogabek.management2.dto.attendance.AttendanceDto;
import com.ogabek.management2.dto.attendance.AttendanceRequest;
import com.ogabek.management2.dto.attendance.MarkAttendanceRequest;
import com.ogabek.management2.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@Tag(name = "Attendance", description = "Attendance management operations")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/mark")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN') or hasRole('DIRECTOR')")
    @Operation(summary = "Mark attendance for a lesson")
    public ResponseEntity<List<AttendanceDto>> markAttendance(
            @Valid @RequestBody MarkAttendanceRequest request) {
        List<AttendanceDto> result = attendanceService.markAttendance(
                request.getLessonId(), 
                request.getAttendances()
        );
        return ResponseEntity.ok(result);
    }

    @GetMapping("/lesson/{lessonId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN') or hasRole('DIRECTOR')")
    @Operation(summary = "Get attendance by lesson")
    public ResponseEntity<List<AttendanceDto>> getAttendanceByLesson(
            @Parameter(description = "Lesson ID") @PathVariable Long lessonId) {
        List<AttendanceDto> attendance = attendanceService.getAttendanceByLesson(lessonId);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/student/{studentId}/group/{groupId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN') or hasRole('DIRECTOR')")
    @Operation(summary = "Get student attendance for a group")
    public ResponseEntity<List<AttendanceDto>> getStudentAttendance(
            @Parameter(description = "Student ID") @PathVariable Long studentId,
            @Parameter(description = "Group ID") @PathVariable Long groupId) {
        List<AttendanceDto> attendance = attendanceService.getStudentAttendance(studentId, groupId);
        return ResponseEntity.ok(attendance);
    }

    @PutMapping("/{attendanceId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN') or hasRole('DIRECTOR')")
    @Operation(summary = "Update attendance record")
    public ResponseEntity<AttendanceDto> updateAttendance(
            @Parameter(description = "Attendance ID") @PathVariable Long attendanceId,
            @Valid @RequestBody AttendanceRequest request) {
        AttendanceDto updated = attendanceService.updateAttendance(attendanceId, request);
        return ResponseEntity.ok(updated);
    }
}