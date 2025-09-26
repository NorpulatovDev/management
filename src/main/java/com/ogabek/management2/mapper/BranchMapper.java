package com.ogabek.management2.mapper;

import com.ogabek.management2.dto.branch.BranchDto;
import com.ogabek.management2.dto.branch.CreateBranchRequest;
import com.ogabek.management2.dto.branch.UpdateBranchRequest;
import com.ogabek.management2.entity.Branch;

public class BranchMapper {
    
    public static BranchDto toDto(Branch branch) {
        if (branch == null) return null;
        return BranchDto.builder()
                .id(branch.getId())
                .name(branch.getName())
                .address(branch.getAddress())
                .phone(branch.getPhone())
                .active(branch.isActive())
                .build();
    }
    
    public static Branch toEntity(CreateBranchRequest request) {
        if (request == null) return null;
        return Branch.builder()
                .name(request.getName())
                .address(request.getAddress())
                .phone(request.getPhone())
                .active(true)
                .build();
    }
    
    public static void updateEntity(Branch branch, UpdateBranchRequest request) {
        if (request == null || branch == null) return;
        
        if (request.getName() != null) {
            branch.setName(request.getName());
        }
        if (request.getAddress() != null) {
            branch.setAddress(request.getAddress());
        }
        if (request.getPhone() != null) {
            branch.setPhone(request.getPhone());
        }
        if (request.getActive() != null) {
            branch.setActive(request.getActive());
        }
    }
}