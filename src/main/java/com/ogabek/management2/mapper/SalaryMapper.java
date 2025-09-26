package com.ogabek.management2.mapper;

import com.ogabek.management2.dto.salary.SalaryDto;
import com.ogabek.management2.dto.salary.CreateSalaryRequest;
import com.ogabek.management2.dto.salary.UpdateSalaryRequest;
import com.ogabek.management2.entity.Salary;
import com.ogabek.management2.entity.Teacher;
import com.ogabek.management2.entity.User;

public class SalaryMapper {
    
    public static SalaryDto toDto(Salary salary) {
        if (salary == null) return null;
        return SalaryDto.builder()
                .id(salary.getId())
                .teacher(TeacherMapper.toDto(salary.getTeacher()))
                .amount(salary.getAmount())
                .salaryMonth(salary.getSalaryMonth())
                .totalLessons(salary.getTotalLessons())
                .percentage(salary.getPercentage())
                .status(salary.getStatus())
                .createdAt(salary.getCreatedAt())
                .processedBy(UserMapper.toDto(salary.getProcessedBy()))
                .build();
    }
    
    public static Salary toEntity(CreateSalaryRequest request, Teacher teacher, User processedBy) {
        if (request == null) return null;
        return Salary.builder()
                .teacher(teacher)
                .amount(request.getAmount())
                .salaryMonth(request.getSalaryMonth())
                .totalLessons(request.getTotalLessons())
                .percentage(request.getPercentage())
                .processedBy(processedBy)
                .build();
    }
    
    public static void updateEntity(Salary salary, UpdateSalaryRequest request) {
        if (request == null || salary == null) return;
        
        if (request.getAmount() != null) {
            salary.setAmount(request.getAmount());
        }
        if (request.getTotalLessons() > 0) {
            salary.setTotalLessons(request.getTotalLessons());
        }
        if (request.getPercentage() != null) {
            salary.setPercentage(request.getPercentage());
        }
        if (request.getStatus() != null) {
            salary.setStatus(request.getStatus());
        }
    }
}