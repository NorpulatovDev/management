package com.ogabek.management2.mapper;

import com.ogabek.management2.dto.group.GroupDto;
import com.ogabek.management2.dto.group.CreateGroupRequest;
import com.ogabek.management2.dto.group.UpdateGroupRequest;
import com.ogabek.management2.entity.Group;
import com.ogabek.management2.entity.Branch;
import com.ogabek.management2.entity.Teacher;
import java.util.stream.Collectors;

public class GroupMapper {
    
    public static GroupDto toDto(Group group) {
        if (group == null) return null;
        return GroupDto.builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .pricePerLesson(group.getPricePerLesson())
                .status(group.getStatus())
                .branch(BranchMapper.toDto(group.getBranch()))
                .teacher(TeacherMapper.toDto(group.getTeacher()))
                .students(group.getStudents().stream()
                        .map(StudentMapper::toDto)
                        .collect(Collectors.toList()))
                .schedules(group.getSchedules().stream()
                        .map(ScheduleMapper::toDto)
                        .collect(Collectors.toList()))
                .build();
    }
    
    public static Group toEntity(CreateGroupRequest request, Branch branch, Teacher teacher) {
        if (request == null) return null;
        return Group.builder()
                .name(request.getName())
                .description(request.getDescription())
                .pricePerLesson(request.getPricePerLesson())
                .branch(branch)
                .teacher(teacher)
                .build();
    }
    
    public static void updateEntity(Group group, UpdateGroupRequest request, Teacher teacher) {
        if (request == null || group == null) return;
        
        if (request.getName() != null) {
            group.setName(request.getName());
        }
        if (request.getDescription() != null) {
            group.setDescription(request.getDescription());
        }
        if (request.getPricePerLesson() != null) {
            group.setPricePerLesson(request.getPricePerLesson());
        }
        if (request.getStatus() != null) {
            group.setStatus(request.getStatus());
        }
        if (teacher != null) {
            group.setTeacher(teacher);
        }
    }
}