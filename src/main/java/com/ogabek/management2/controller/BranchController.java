package com.ogabek.management2.controller;

import com.ogabek.management2.dto.branch.BranchDto;
import com.ogabek.management2.dto.branch.CreateBranchRequest;
import com.ogabek.management2.dto.branch.UpdateBranchRequest;
import com.ogabek.management2.dto.dashboard.DashboardStatsDto;
import com.ogabek.management2.service.BranchService;
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
@RequestMapping("/api/branches")
@RequiredArgsConstructor
@Tag(name = "Branches", description = "Branch management operations")
public class BranchController {

    private final BranchService branchService;

    @PostMapping
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Create a new branch")
    public ResponseEntity<BranchDto> createBranch(@Valid @RequestBody CreateBranchRequest request) {
        BranchDto branch = branchService.createBranch(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(branch);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Update branch information")
    public ResponseEntity<BranchDto> updateBranch(
            @Parameter(description = "Branch ID") @PathVariable Long id,
            @Valid @RequestBody UpdateBranchRequest request) {
        BranchDto updated = branchService.updateBranch(id, request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN') or hasRole('TEACHER')")
    @Operation(summary = "Get branch by ID")
    public ResponseEntity<BranchDto> getBranchById(
            @Parameter(description = "Branch ID") @PathVariable Long id) {
        BranchDto branch = branchService.findById(id);
        return ResponseEntity.ok(branch);
    }

    @GetMapping
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN') or hasRole('TEACHER')")
    @Operation(summary = "Get all branches")
    public ResponseEntity<List<BranchDto>> getAllBranches() {
        List<BranchDto> branches = branchService.findAll();
        return ResponseEntity.ok(branches);
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN') or hasRole('TEACHER')")
    @Operation(summary = "Get all active branches")
    public ResponseEntity<List<BranchDto>> getActiveBranches() {
        List<BranchDto> branches = branchService.findActiveBranches();
        return ResponseEntity.ok(branches);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get branch by name")
    public ResponseEntity<BranchDto> getBranchByName(
            @Parameter(description = "Branch name") @PathVariable String name) {
        BranchDto branch = branchService.findByName(name);
        return ResponseEntity.ok(branch);
    }

    @GetMapping("/{id}/statistics")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get branch statistics")
    public ResponseEntity<DashboardStatsDto> getBranchStatistics(
            @Parameter(description = "Branch ID") @PathVariable Long id) {
        DashboardStatsDto stats = branchService.getBranchStatistics(id);
        return ResponseEntity.ok(stats);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DIRECTOR')")
    @Operation(summary = "Delete branch")
    public ResponseEntity<Void> deleteBranch(
            @Parameter(description = "Branch ID") @PathVariable Long id) {
        branchService.deleteBranch(id);
        return ResponseEntity.noContent().build();
    }
}