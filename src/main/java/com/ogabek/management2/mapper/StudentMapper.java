package com.ogabek.management2.mapper;

import com.ogabek.management2.dto.student.StudentDto;
import com.ogabek.management2.dto.student.CreateStudentRequest;
import com.ogabek.management2.dto.student.UpdateStudentRequest;
import com.ogabek.management2.entity.Student;
import com.ogabek.management2.entity.Branch;
import com.ogabek.management2.entity.User;

public class StudentMapper {
    
    public static
    com.ogabek.management2.dto.student.StudentDto toDto(Student student) {
        if (student == null) return null;
        return StudentDto.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .phoneNumber(student.getPhoneNumber())
                .parentPhoneNumber(student.getParentPhoneNumber())
                .balance(student.getBalance())
                .branch(BranchMapper.toDto(student.getBranch()))
                .user(UserMapper.toDto(student.getUser()))
                .build();
    }
    
    public static Student toEntity(CreateStudentRequest request, Branch branch, User user) {
        if (request == null) return null;
        return Student.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .parentPhoneNumber(request.getParentPhoneNumber())
                .balance(request.getInitialBalance())
                .branch(branch)
                .user(user)
                .build();
    }
    
    public static void updateEntity(Student student, UpdateStudentRequest request, Branch branch) {
        if (request == null || student == null) return;
        
        if (request.getFirstName() != null) {
            student.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            student.setLastName(request.getLastName());
        }
        if (request.getPhoneNumber() != null) {
            student.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getParentPhoneNumber() != null) {
            student.setParentPhoneNumber(request.getParentPhoneNumber());
        }
        if (branch != null) {
            student.setBranch(branch);
        }
    }
}