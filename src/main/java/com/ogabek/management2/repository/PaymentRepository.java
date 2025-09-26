package com.ogabek.management2.repository;

import com.ogabek.management2.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByStudentIdOrderByCreatedAtDesc(Long studentId);
    
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.student.id = :studentId")
    BigDecimal getTotalPaymentsByStudent(Long studentId);
    
    @Query("SELECT p FROM Payment p WHERE p.student.user.username = :username ORDER BY p.createdAt DESC")
    List<Payment> findByStudentUsernameOrderByCreatedAtDesc(String username);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.createdAt BETWEEN :start AND :end")
    BigDecimal getTotalPaymentsByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT p FROM Payment p WHERE p.student.branch.id = :branchId AND p.createdAt BETWEEN :start AND :end")
    List<Payment> findByStudentBranchIdAndCreatedAtBetween(@Param("branchId") Long branchId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.student.branch.id = :branchId AND p.createdAt BETWEEN :start AND :end")
    BigDecimal getTotalPaymentsByBranchAndDateRange(@Param("branchId") Long branchId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}