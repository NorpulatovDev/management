package com.ogabek.management2.service;

import com.ogabek.management2.dto.attendance.AttendanceDto;
import com.ogabek.management2.dto.attendance.AttendanceRequest;
import java.util.List;

public interface AttendanceService {
    List<AttendanceDto> markAttendance(Long lessonId, List<AttendanceRequest> attendanceList);
    List<AttendanceDto> getAttendanceByLesson(Long lessonId);
    List<AttendanceDto> getStudentAttendance(Long studentId, Long groupId);
    AttendanceDto updateAttendance(Long attendanceId, AttendanceRequest request);
}