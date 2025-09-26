package com.ogabek.management2.repository;

import com.ogabek.management2.entity.Salary;
import com.ogabek.management2.entity.SalaryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SalaryRepository extends JpaRepository<Salary, Long> {
    
    // Find salaries by teacher
    List<Salary> findByTeacherId(Long teacherId);
    
    // Find salaries by teacher and month
    Optional<Salary> findByTeacherIdAndSalaryMonth(Long teacherId, LocalDate salaryMonth);
    
    // Find salaries by status
    List<Salary> findByStatus(SalaryStatus status);
    
    // Find salaries by month
    List<Salary> findBySalaryMonth(LocalDate salaryMonth);
    
    // Find salaries by teacher branch
    @Query("SELECT s FROM Salary s WHERE s.teacher.branch.id = :branchId")
    List<Salary> findByTeacherBranchId(@Param("branchId") Long branchId);
    
    // Find salaries by branch and month
    @Query("SELECT s FROM Salary s WHERE s.teacher.branch.id = :branchId AND s.salaryMonth = :month")
    List<Salary> findByTeacherBranchIdAndMonth(@Param("branchId") Long branchId, @Param("month") LocalDate month);
    
    // Get total salaries by month
    @Query("SELECT COALESCE(SUM(s.amount), 0) FROM Salary s WHERE s.salaryMonth = :month AND s.status = 'PAID'")
    BigDecimal getTotalSalariesByMonth(@Param("month") LocalDate month);
    
    // Get total salaries by branch and month
    @Query("SELECT COALESCE(SUM(s.amount), 0) FROM Salary s WHERE s.teacher.branch.id = :branchId AND s.salaryMonth = :month AND s.status = 'PAID'")
    BigDecimal getTotalSalariesByBranchAndMonth(@Param("branchId") Long branchId, @Param("month") LocalDate month);
    
    // Check if salary exists for teacher and month
    boolean existsByTeacherIdAndSalaryMonth(Long teacherId, LocalDate salaryMonth);
    
    // Find pending salaries
    @Query("SELECT s FROM Salary s WHERE s.status = 'PENDING' ORDER BY s.salaryMonth DESC, s.teacher.lastName ASC")
    List<Salary> findPendingSalaries();
    
    // Get salary statistics by teacher
    @Query("SELECT s.teacher.id, s.teacher.firstName, s.teacher.lastName, " +
           "COUNT(s) as totalSalaries, " +
           "SUM(CASE WHEN s.status = 'PAID' THEN s.amount ELSE 0 END) as totalPaid, " +
           "SUM(CASE WHEN s.status = 'PENDING' THEN s.amount ELSE 0 END) as totalPending " +
           "FROM Salary s " +
           "WHERE s.teacher.branch.id = :branchId " +
           "GROUP BY s.teacher.id, s.teacher.firstName, s.teacher.lastName")
    List<Object[]> getSalaryStatisticsByBranch(@Param("branchId") Long branchId);
}