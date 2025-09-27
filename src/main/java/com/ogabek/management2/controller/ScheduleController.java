package com.ogabek.management2.controller;

import com.ogabek.management2.dto.schedule.*;
import com.ogabek.management2.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
@Tag(name = "Schedules", description = "Schedule management operations")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/group/{groupId}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Create schedule for group")
    public ResponseEntity<ScheduleDto> createSchedule(
            @Parameter(description = "Group ID") @PathVariable Long groupId,
            @Valid @RequestBody CreateScheduleRequest request) {
        ScheduleDto schedule = scheduleService.createSchedule(request, groupId);
        return ResponseEntity.status(HttpStatus.CREATED).body(schedule);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Update schedule")
    public ResponseEntity<ScheduleDto> updateSchedule(
            @Parameter(description = "Schedule ID") @PathVariable Long id,
            @Valid @RequestBody UpdateScheduleRequest request) {
        ScheduleDto updated = scheduleService.updateSchedule(id, request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN') or hasRole('TEACHER')")
    @Operation(summary = "Get schedule by ID")
    public ResponseEntity<ScheduleDto> getScheduleById(
            @Parameter(description = "Schedule ID") @PathVariable Long id) {
        ScheduleDto schedule = scheduleService.findById(id);
        return ResponseEntity.ok(schedule);
    }

    @GetMapping("/group/{groupId}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN') or hasRole('TEACHER')")
    @Operation(summary = "Get schedules by group")
    public ResponseEntity<List<ScheduleDto>> getSchedulesByGroup(
            @Parameter(description = "Group ID") @PathVariable Long groupId) {
        List<ScheduleDto> schedules = scheduleService.findByGroup(groupId);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/group/{groupId}/active")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN') or hasRole('TEACHER')")
    @Operation(summary = "Get active schedules by group")
    public ResponseEntity<List<ScheduleDto>> getActiveSchedulesByGroup(
            @Parameter(description = "Group ID") @PathVariable Long groupId) {
        List<ScheduleDto> schedules = scheduleService.findActiveByGroup(groupId);
        return ResponseEntity.ok(schedules);
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Deactivate schedule")
    public ResponseEntity<Void> deactivateSchedule(
            @Parameter(description = "Schedule ID") @PathVariable Long id) {
        scheduleService.deactivateSchedule(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Activate schedule")
    public ResponseEntity<Void> activateSchedule(
            @Parameter(description = "Schedule ID") @PathVariable Long id) {
        scheduleService.activateSchedule(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DIRECTOR')")
    @Operation(summary = "Delete schedule")
    public ResponseEntity<Void> deleteSchedule(
            @Parameter(description = "Schedule ID") @PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }
}