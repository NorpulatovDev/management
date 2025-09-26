package com.ogabek.management2.serviceImpl;

import com.ogabek.management2.dto.attendance.AttendanceDto;
import com.ogabek.management2.dto.attendance.AttendanceRequest;
import com.ogabek.management2.entity.*;
import com.ogabek.management2.exception.ApiException;
import com.ogabek.management2.exception.ResourceNotFoundException;
import com.ogabek.management2.mapper.AttendanceMapper;
import com.ogabek.management2.repository.*;
import com.ogabek.management2.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;

    @Override
    public List<AttendanceDto> markAttendance(Long lessonId, List<AttendanceRequest> attendanceList) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        if (lesson.getStatus() == LessonStatus.COMPLETED) {
            throw new ApiException("Cannot modify attendance for completed lesson");
        }

        List<AttendanceDto> results = new ArrayList<>();

        for (AttendanceRequest request : attendanceList) {
            Student student = studentRepository.findById(request.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

            // Verify student belongs to this group
            if (!lesson.getGroup().getStudents().contains(student)) {
                throw new ApiException("Student is not enrolled in this group");
            }

            // Check if attendance already exists
            Optional<Attendance> existingAttendance = attendanceRepository
                    .findByLessonIdAndStudentId(lessonId, request.getStudentId());

            Attendance attendance;
            if (existingAttendance.isPresent()) {
                // Update existing attendance
                attendance = existingAttendance.get();
                attendance.setStatus(request.getStatus());
            } else {
                // Create new attendance record
                attendance = AttendanceMapper.toEntity(request, lesson, student);
            }

            attendanceRepository.save(attendance);
            results.add(AttendanceMapper.toDto(attendance));
        }

        return results;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceDto> getAttendanceByLesson(Long lessonId) {
        return attendanceRepository.findByLessonId(lessonId)
                .stream()
                .map(AttendanceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceDto> getStudentAttendance(Long studentId, Long groupId) {
        return attendanceRepository.findByStudentIdAndLessonGroupId(studentId, groupId)
                .stream()
                .map(AttendanceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AttendanceDto updateAttendance(Long attendanceId, AttendanceRequest request) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found"));

        if (attendance.getLesson().getStatus() == LessonStatus.COMPLETED) {
            throw new ApiException("Cannot modify attendance for completed lesson");
        }

        attendance.setStatus(request.getStatus());
        attendanceRepository.save(attendance