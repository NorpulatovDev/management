package com.ogabek.management2.controller;

import com.ogabek.management2.dto.dashboard.DashboardStatsDto;
import com.ogabek.management2.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Dashboard statistics operations")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/overall")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get overall system statistics")
    public ResponseEntity<DashboardStatsDto> getOverallStatistics() {
        DashboardStatsDto stats = dashboardService.getOverallStatistics();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/branch/{branchId}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get branch statistics")
    public ResponseEntity<DashboardStatsDto> getBranchStatistics(
            @Parameter(description = "Branch ID") @PathVariable Long branchId) {
        DashboardStatsDto stats = dashboardService.getBranchStatistics(branchId);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/monthly")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get monthly statistics")
    public ResponseEntity<DashboardStatsDto> getMonthlyStatistics(
            @Parameter(description = "Month (YYYY-MM-DD format)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate month) {
        DashboardStatsDto stats = dashboardService.getMonthlyStatistics(month);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/branch/{branchId}/monthly")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get branch monthly statistics")
    public ResponseEntity<DashboardStatsDto> getBranchMonthlyStatistics(
            @Parameter(description = "Branch ID") @PathVariable Long branchId,
            @Parameter(description = "Month (YYYY-MM-DD format)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate month) {
        DashboardStatsDto stats = dashboardService.getBranchMonthlyStatistics(branchId, month);
        return ResponseEntity.ok(stats);
    }
}