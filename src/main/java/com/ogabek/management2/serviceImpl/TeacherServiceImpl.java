package com.ogabek.management2.serviceImpl;

import com.ogabek.management2.dto.teacher.CreateTeacherRequest;
import com.ogabek.management2.dto.teacher.TeacherDto;
import com.ogabek.management2.dto.teacher.UpdateTeacherRequest;
import com.ogabek.management2.entity.*;
import com.ogabek.management2.exception.ApiException;
import com.ogabek.management2.exception.ResourceNotFoundException;
import com.ogabek.management2.mapper.TeacherMapper;
import com.ogabek.management2.repository.*;
import com.ogabek.management2.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public TeacherDto createTeacher(CreateTeacherRequest request) {
        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ApiException("Username already exists: " + request.getUsername());
        }

        // Find branch
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));

        // Create user account
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.TEACHER)
                .enabled(true)
                .build();
        userRepository.save(user);

        // Create teacher
        Teacher teacher = TeacherMapper.toEntity(request, branch, user);
        teacherRepository.save(teacher);

        return TeacherMapper.toDto(teacher);
    }

    @Override
    public TeacherDto updateTeacher(Long id, UpdateTeacherRequest request) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        Branch branch = null;
        if (request.getBranchId() != null) {
            branch = branchRepository.findById(request.getBranchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));
        }

        TeacherMapper.updateEntity(teacher, request, branch);
        teacherRepository.save(teacher);

        return TeacherMapper.toDto(teacher);
    }

    @Override
    @Transactional(readOnly = true)
    public TeacherDto findById(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        return TeacherMapper.toDto(teacher);
    }

    @Override
    @Transactional(readOnly = true)
    public TeacherDto findByUsername(String username) {
        Teacher teacher = teacherRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + username));
        return TeacherMapper.toDto(teacher);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeacherDto> findAll() {
        return teacherRepository.findAll()
                .stream()
                .map(TeacherMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeacherDto> findByBranch(Long branchId) {
        return teacherRepository.findByBranchId(branchId)
                .stream()
                .map(TeacherMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTeacher(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        
        // Check if teacher has any groups
        if (!teacher.getGroups().isEmpty()) {
            throw new ApiException("Cannot delete teacher with assigned groups");
        }
        
        teacherRepository.delete(teacher);
    }
}