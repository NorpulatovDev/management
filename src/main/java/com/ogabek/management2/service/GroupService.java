package com.ogabek.management2.service;

import com.ogabek.management2.dto.group.*;
import java.util.List;

public interface GroupService {
    GroupDto createGroup(CreateGroupRequest request);
    GroupDto updateGroup(Long id, UpdateGroupRequest request);
    GroupDto findById(Long id);
    List<GroupDto> findAll();
    List<GroupDto> findByBranch(Long branchId);
    List<GroupDto> findByTeacher(String teacherUsername);
    GroupDto addStudentsToGroup(Long groupId, AddStudentsToGroupRequest request);
    GroupDto removeStudentFromGroup(Long groupId, Long studentId);
    void deleteGroup(Long id);
}