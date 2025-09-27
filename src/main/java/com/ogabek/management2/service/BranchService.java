package com.ogabek.management2.service;

import com.ogabek.management2.dto.branch.BranchDto;
import com.ogabek.management2.dto.branch.CreateBranchRequest;
import com.ogabek.management2.dto.branch.UpdateBranchRequest;
import com.ogabek.management2.dto.dashboard.DashboardStatsDto;

import java.util.List;

public interface BranchService {
    BranchDto createBranch(CreateBranchRequest request);
    BranchDto updateBranch(Long id, UpdateBranchRequest request);
    BranchDto findById(Long id);
    List<BranchDto> findAll();
    List<BranchDto> findActiveBranches();
    BranchDto findByName(String name);
    void deleteBranch(Long id);
    DashboardStatsDto getBranchStatistics(Long branchId);
}