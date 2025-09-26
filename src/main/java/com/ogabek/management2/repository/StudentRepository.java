package com.ogabek.management2.repository;

import com.ogabek.management2.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUsername(String username);

    @Query("SELECT s FROM Student s WHERE s.branch.id = :branchId")
    List<Student> findByBranchId(Long branchId);

    @Query("SELECT s from Student s join s.groups g where g.id = :groupId")
    List<Student> findByGroupId(Long groupId);

    // Add these methods to the existing StudentRepository

    @Query("SELECT s FROM Student s WHERE s.branch.id = :branchId AND s.balance < :minBalance")
    List<Student> findByBranchIdAndBalanceLessThan(@Param("branchId") Long branchId, @Param("minBalance") BigDecimal minBalance);

    @Query("SELECT COUNT(s) FROM Student s WHERE s.branch.id = :branchId")
    Long countByBranchId(@Param("branchId") Long branchId);

    boolean existsByUserUsername(String username);

    boolean existsByPhoneNumber(String phoneNumber);
}
