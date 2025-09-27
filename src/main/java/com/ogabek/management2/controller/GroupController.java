package com.ogabek.management2.controller;

import com.ogabek.management2.dto.group.*;
import com.ogabek.management2.service.GroupService;
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
@RequestMapping("/api/groups")
@RequiredArgsConstructor
@Tag(name = "Groups", description = "Group management operations")
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Create a new group")
    public ResponseEntity<GroupDto> createGroup(@Valid @RequestBody CreateGroupRequest request) {
        GroupDto group = groupService.createGroup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(group);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Update group")
    public ResponseEntity<GroupDto> updateGroup(
            @Parameter(description = "Group ID") @PathVariable Long id,
            @Valid @RequestBody UpdateGroupRequest request) {
        GroupDto updated = groupService.updateGroup(id, request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN') or hasRole('TEACHER')")
    @Operation(summary = "Get group by ID")
    public ResponseEntity<GroupDto> getGroupById(
            @Parameter(description = "Group ID") @PathVariable Long id) {
        GroupDto group = groupService.findById(id);
        return ResponseEntity.ok(group);
    }

    @GetMapping
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get all groups")
    public ResponseEntity<List<GroupDto>> getAllGroups() {
        List<GroupDto> groups = groupService.findAll();
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/branch/{branchId}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get groups by branch")
    public ResponseEntity<List<GroupDto>> getGroupsByBranch(
            @Parameter(description = "Branch ID") @PathVariable Long branchId) {
        List<GroupDto> groups = groupService.findByBranch(branchId);
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/my-groups")
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Get groups assigned to current teacher")
    public ResponseEntity<List<GroupDto>> getMyGroups(Authentication authentication) {
        List<GroupDto> groups = groupService.findByTeacher(authentication.getName());
        return ResponseEntity.ok(groups);
    }

    @PostMapping("/{id}/students")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Add students to group")
    public ResponseEntity<GroupDto> addStudentsToGroup(
            @Parameter(description = "Group ID") @PathVariable Long id,
            @Valid @RequestBody AddStudentsToGroupRequest request) {
        GroupDto updated = groupService.addStudentsToGroup(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{groupId}/students/{studentId}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Remove student from group")
    public ResponseEntity<GroupDto> removeStudentFromGroup(
            @Parameter(description = "Group ID") @PathVariable Long groupId,
            @Parameter(description = "Student ID") @PathVariable Long studentId) {
        GroupDto updated = groupService.removeStudentFromGroup(groupId, studentId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DIRECTOR')")
    @Operation(summary = "Delete group")
    public ResponseEntity<Void> deleteGroup(
            @Parameter(description = "Group ID") @PathVariable Long id) {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }
}