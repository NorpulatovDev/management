package com.ogabek.management2.service;

import com.ogabek.management2.dto.teacher.TeacherDto;
import com.ogabek.management2.dto.teacher.CreateTeacherRequest;
import com.ogabek.management2.dto.teacher.UpdateTeacherRequest;
import java.util.List;

public interface TeacherService {
    TeacherDto createTeacher(CreateTeacherRequest request);
    TeacherDto updateTeacher(Long id, UpdateTeacherRequest request);
    TeacherDto findById(Long id);
    TeacherDto findByUsername(String username);
    List<TeacherDto> findAll();
    List<TeacherDto> findByBranch(Long branchId);
    void deleteTeacher(Long id);
}