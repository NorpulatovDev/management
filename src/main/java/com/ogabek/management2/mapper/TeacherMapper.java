package com.ogabek.management2.mapper;

import com.ogabek.management2.dto.teacher.TeacherDto;
import com.ogabek.management2.dto.teacher.CreateTeacherRequest;
import com.ogabek.management2.dto.teacher.UpdateTeacherRequest;
import com.ogabek.management2.entity.Teacher;
import com.ogabek.management2.entity.Branch;
import com.ogabek.management2.entity.User;

public class TeacherMapper {
    
    public static TeacherDto toDto(Teacher teacher) {
        if (teacher == null) return null;
        return TeacherDto.builder()
                .id(teacher.getId())
                .firstName(teacher.getFirstName())
                .lastName(teacher.getLastName())
                .phoneNumber(teacher.getPhoneNumber())
                .email(teacher.getEmail())
                .salaryPercentage(teacher.getSalaryPercentage())
                .branch(BranchMapper.toDto(teacher.getBranch()))
                .user(UserMapper.toDto(teacher.getUser()))
                .build();
    }
    
    public static Teacher toEntity(CreateTeacherRequest request, Branch branch, User user) {
        if (request == null) return null;
        return Teacher.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .salaryPercentage(request.getSalaryPercentage())
                .branch(branch)
                .user(user)
                .build();
    }
    
    public static void updateEntity(Teacher teacher, UpdateTeacherRequest request, Branch branch) {
        if (request == null || teacher == null) return;
        
        if (request.getFirstName() != null) {
            teacher.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            teacher.setLastName(request.getLastName());
        }
        if (request.getPhoneNumber() != null) {
            teacher.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getEmail() != null) {
            teacher.setEmail(request.getEmail());
        }
        if (request.getSalaryPercentage() != null) {
            teacher.setSalaryPercentage(request.getSalaryPercentage());
        }
        if (branch != null) {
            teacher.setBranch(branch);
        }
    }
}