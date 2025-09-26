package com.ogabek.management2.repository;

import com.ogabek.management2.entity.Expense;
import com.ogabek.management2.entity.ExpenseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    
    // Find expenses by branch
    List<Expense> findByBranchId(Long branchId);
    
    // Find expenses by type
    List<Expense> findByType(ExpenseType type);
    
    // Find expenses by branch and type
    List<Expense> findByBranchIdAndType(Long branchId, ExpenseType type);
    
    // Find expenses by date range
    List<Expense> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    // Find expenses by branch and date range
    List<Expense> findByBranchIdAndCreatedAtBetween(Long branchId, LocalDateTime start, LocalDateTime end);
    
    // Get total expenses by branch
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.branch.id = :branchId")
    BigDecimal getTotalExpensesByBranch(@Param("branchId") Long branchId);
    
    // Get total expenses by type
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.type = :type")
    BigDecimal getTotalExpensesByType(@Param("type") ExpenseType type);
    
    // Get total expenses by branch and date range
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.branch.id = :branchId AND e.createdAt BETWEEN :start AND :end")
    BigDecimal getTotalExpensesByBranchAndDateRange(@Param("branchId") Long branchId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    // Find expenses by created by user
    List<Expense> findByCreatedById(Long userId);
    
    // Get monthly expenses summary
    @Query("SELECT e.type, SUM(e.amount) FROM Expense e WHERE e.branch.id = :branchId AND e.createdAt BETWEEN :start AND :end GROUP BY e.type")
    List<Object[]> getMonthlyExpensesSummary(@Param("branchId") Long branchId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.createdAt BETWEEN :start AND :end")
    BigDecimal getTotalExpensesByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}