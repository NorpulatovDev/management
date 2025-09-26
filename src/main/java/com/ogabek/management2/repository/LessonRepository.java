package com.ogabek.management2.repository;

import com.ogabek.management2.entity.Lesson;
import com.ogabek.management2.entity.LessonStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByGroupIdAndLessonDateBetween(Long groupId, LocalDate start, LocalDate end);
    
    @Query("SELECT l FROM Lesson l WHERE l.group.teacher.user.username = :teacherUsername AND l.lessonDate BETWEEN :start AND :end")
    List<Lesson> findByTeacherUsernameAndDateBetween(String teacherUsername, LocalDate start, LocalDate end);
    
    List<Lesson> findByStatusAndCharged(LessonStatus status, boolean charged);
    
    boolean existsByGroupIdAndLessonDate(Long groupId, LocalDate lessonDate);
    
    @Query("SELECT l FROM Lesson l WHERE l.group.branch.id = :branchId")
    List<Lesson> findByBranchId(Long branchId);

    // Add these methods to the existing LessonRepository

    @Query("SELECT COUNT(l) FROM Lesson l WHERE l.group.branch.id = :branchId AND l.status = :status")
    Long countByBranchIdAndStatus(@Param("branchId") Long branchId, @Param("status") LessonStatus status);

    @Query("SELECT l FROM Lesson l WHERE l.status = 'COMPLETED' AND l.charged = false")
    List<Lesson> findCompletedUnchargedLessons();

    @Query("SELECT l FROM Lesson l WHERE l.group.id = :groupId AND l.lessonDate = :date")
    Optional<Lesson> findByGroupIdAndLessonDate(@Param("groupId") Long groupId, @Param("date") LocalDate date);

    Long countByStatus(LessonStatus status);

    @Query("SELECT COUNT(l) FROM Lesson l WHERE l.lessonDate BETWEEN :start AND :end AND l.status = :status")
    Long countByDateRangeAndStatus(@Param("start") LocalDate start, @Param("end") LocalDate end, @Param("status") LessonStatus status);

    @Query("SELECT COUNT(l) FROM Lesson l WHERE l.group.branch.id = :branchId AND l.lessonDate BETWEEN :start AND :end AND l.status = :status")
    Long countByBranchIdAndDateRangeAndStatus(@Param("branchId") Long branchId, @Param("start") LocalDate start, @Param("end") LocalDate end, @Param("status") LessonStatus status);
}