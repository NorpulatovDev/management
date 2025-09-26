package com.ogabek.management2.repository;

import com.ogabek.management2.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByLessonId(Long lessonId);
    List<Attendance> findByStudentIdAndLessonGroupId(Long studentId, Long groupId);
    Optional<Attendance> findByLessonIdAndStudentId(Long lessonId, Long studentId);
    boolean existsByLessonIdAndStudentId(Long lessonId, Long studentId);
}