package com.ogabek.management2.serviceImpl;

import com.ogabek.management2.dto.group.AddStudentsToGroupRequest;
import com.ogabek.management2.dto.group.CreateGroupRequest;
import com.ogabek.management2.dto.group.GroupDto;
import com.ogabek.management2.dto.group.UpdateGroupRequest;
import com.ogabek.management2.entity.*;
import com.ogabek.management2.exception.ApiException;
import com.ogabek.management2.exception.ResourceNotFoundException;
import com.ogabek.management2.mapper.GroupMapper;
import com.ogabek.management2.mapper.ScheduleMapper;
import com.ogabek.management2.repository.*;
import com.ogabek.management2.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final BranchRepository branchRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final ScheduleRepository scheduleRepository;

    @Override
    public GroupDto createGroup(CreateGroupRequest request) {
        // Find branch
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));

        // Find teacher
        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        // Create group
        Group group = GroupMapper.toEntity(request, branch, teacher);
        groupRepository.save(group);

        // Add students if provided
        if (request.getStudentIds() != null && !request.getStudentIds().isEmpty()) {
            Set<Student> students = request.getStudentIds().stream()
                    .map(studentId -> studentRepository.findById(studentId)
                            .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentId)))
                    .collect(Collectors.toSet());
            group.setStudents(students);
        }

        // Add schedules if provided
        if (request.getSchedules() != null && !request.getSchedules().isEmpty()) {
            Set<Schedule> schedules = request.getSchedules().stream()
                    .map(scheduleRequest -> ScheduleMapper.toEntity(scheduleRequest, group))
                    .collect(Collectors.toSet());
            scheduleRepository.saveAll(schedules);
            group.setSchedules(schedules);
        }

        groupRepository.save(group);
        return GroupMapper.toDto(group);
    }

    @Override
    public GroupDto updateGroup(Long id, UpdateGroupRequest request) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        Teacher teacher = null;
        if (request.getTeacherId() != null) {
            teacher = teacherRepository.findById(request.getTeacherId())
                    .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        }

        GroupMapper.updateEntity(group, request, teacher);
        groupRepository.save(group);

        return GroupMapper.toDto(group);
    }

    @Override
    @Transactional(readOnly = true)
    public GroupDto findById(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        return GroupMapper.toDto(group);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupDto> findAll() {
        return groupRepository.findAll()
                .stream()
                .map(GroupMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupDto> findByBranch(Long branchId) {
        return groupRepository.findByBranchId(branchId)
                .stream()
                .map(GroupMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupDto> findByTeacher(String teacherUsername) {
        return groupRepository.findByTeacherUsername(teacherUsername)
                .stream()
                .map(GroupMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public GroupDto addStudentsToGroup(Long groupId, AddStudentsToGroupRequest request) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        List<Student> students = request.getStudentIds().stream()
                .map(studentId -> studentRepository.findById(studentId)
                        .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentId)))
                .collect(Collectors.toList());

        group.getStudents().addAll(students);
        groupRepository.save(group);

        return GroupMapper.toDto(group);
    }

    @Override
    public GroupDto removeStudentFromGroup(Long groupId, Long studentId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        if (!group.getStudents().contains(student)) {
            throw new ApiException("Student is not in this group");
        }

        group.getStudents().remove(student);
        groupRepository.save(group);

        return GroupMapper.toDto(group);
    }

    @Override
    public void deleteGroup(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        
        // Check if group has any lessons
        if (!group.getLessons().isEmpty()) {
            throw new ApiException("Cannot delete group with lessons. Set status to COMPLETED instead.");
        }
        
        groupRepository.delete(group);
    }
}