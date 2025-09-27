package com.ogabek.management2.serviceImpl;

import com.ogabek.management2.dto.branch.BranchDto;
import com.ogabek.management2.dto.branch.CreateBranchRequest;
import com.ogabek.management2.dto.branch.UpdateBranchRequest;
import com.ogabek.management2.dto.dashboard.DashboardStatsDto;
import com.ogabek.management2.entity.Branch;
import com.ogabek.management2.exception.ApiException;
import com.ogabek.management2.exception.ResourceNotFoundException;
import com.ogabek.management2.mapper.BranchMapper;
import com.ogabek.management2.repository.*;
import com.ogabek.management2.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final GroupRepository groupRepository;
    private final PaymentRepository paymentRepository;
    private final ExpenseRepository expenseRepository;

    @Override
    public BranchDto createBranch(CreateBranchRequest request) {
        // Check if branch name already exists
        if (branchRepository.existsByName(request.getName())) {
            throw new ApiException("Branch name already exists: " + request.getName());
        }

        // Check if phone already exists
        if (branchRepository.existsByPhone(request.getPhone())) {
            throw new ApiException("Phone number already exists: " + request.getPhone());
        }

        Branch branch = BranchMapper.toEntity(request);
        branchRepository.save(branch);

        return BranchMapper.toDto(branch);
    }

    @Override
    public BranchDto updateBranch(Long id, UpdateBranchRequest request) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));

        // Check if new name conflicts with existing branches
        if (request.getName() != null && !request.getName().equals(branch.getName())) {
            if (branchRepository.existsByName(request.getName())) {
                throw new ApiException("Branch name already exists: " + request.getName());
            }
        }

        // Check if new phone conflicts with existing branches
        if (request.getPhone() != null && !request.getPhone().equals(branch.getPhone())) {
            if (branchRepository.existsByPhone(request.getPhone())) {
                throw new ApiException("Phone number already exists: " + request.getPhone());
            }
        }

        BranchMapper.updateEntity(branch, request);
        branchRepository.save(branch);

        return BranchMapper.toDto(branch);
    }

    @Override
    @Transactional(readOnly = true)
    public BranchDto findById(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));
        return BranchMapper.toDto(branch);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BranchDto> findAll() {
        return branchRepository.findAll()
                .stream()
                .map(BranchMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BranchDto> findActiveBranches() {
        return branchRepository.findByActiveTrue()
                .stream()
                .map(BranchMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BranchDto findByName(String name) {
        Branch branch = branchRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found: " + name));
        return BranchMapper.toDto(branch);
    }

    @Override
    public void deleteBranch(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));

        // Check if branch has any students, teachers, or groups
        if (!branch.getStudents().isEmpty()) {
            throw new ApiException("Cannot delete branch with students. Move students first.");
        }
        if (!branch.getTeachers().isEmpty()) {
            throw new ApiException("Cannot delete branch with teachers. Move teachers first.");
        }
        if (!branch.getGroups().isEmpty()) {
            throw new ApiException("Cannot delete branch with groups. Remove groups first.");
        }

        branchRepository.delete(branch);
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardStatsDto getBranchStatistics(Long branchId) {
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atTime(23, 59, 59);

        int totalStudents = studentRepository.countByBranchId(branchId).intValue();
        int totalTeachers = teacherRepository.countByBranchId(branchId).intValue();
        int totalGroups = groupRepository.countByBranchId(branchId).intValue();
        
        BigDecimal totalRevenue = paymentRepository.getTotalPaymentsByBranchAndDateRange(branchId, startOfMonth, endOfMonth);
        BigDecimal totalExpenses = expenseRepository.getTotalExpensesByBranchAndDateRange(branchId, startOfMonth, endOfMonth);
        BigDecimal netProfit = totalRevenue.subtract(totalExpenses);

        return DashboardStatsDto.builder()
                .totalStudents(totalStudents)
                .totalTeachers(totalTeachers)
                .totalGroups(totalGroups)
                .totalRevenue(totalRevenue)
                .totalExpenses(totalExpenses)
                .netProfit(netProfit)
                .build();
    }
}