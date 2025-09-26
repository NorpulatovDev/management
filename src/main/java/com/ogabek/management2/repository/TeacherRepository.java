package com.ogabek.management2.repository;

import com.ogabek.management2.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByUsername(String username);

    @Query("select t from Teacher t where t.branch.id = :branchId")
    List<Teacher> findByBranchId(Long branchId);

    // Add these methods to the existing TeacherRepository

    @Query("SELECT COUNT(t) FROM Teacher t WHERE t.branch.id = :branchId")
    Long countByBranchId(@Param("branchId") Long branchId);

    boolean existsByUserUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT t FROM Teacher t WHERE t.branch.id = :branchId AND t.salaryPercentage > 0")
    List<Teacher> findActivePaidTeachersByBranch(@Param("branchId") Long branchId);
}
