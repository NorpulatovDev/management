package com.ogabek.management2.repository;

import com.ogabek.management2.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    
    // Find active branches
    List<Branch> findByActiveTrue();
    
    // Find branches by name (case insensitive)
    @Query("SELECT b FROM Branch b WHERE LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Branch> findByNameContainingIgnoreCase(@Param("name") String name);
    
    // Find branch by exact name
    Optional<Branch> findByName(String name);
    
    // Find branches by phone
    Optional<Branch> findByPhone(String phone);
    
    // Check if branch name exists (for validation)
    boolean existsByName(String name);
    
    // Check if phone exists (for validation)
    boolean existsByPhone(String phone);
    
    // Find branches by address containing text
    @Query("SELECT b FROM Branch b WHERE LOWER(b.address) LIKE LOWER(CONCAT('%', :address, '%'))")
    List<Branch> findByAddressContainingIgnoreCase(@Param("address") String address);
    
    // Get branch statistics
    @Query("SELECT b.id, b.name, " +
           "COUNT(DISTINCT s) as studentCount, " +
           "COUNT(DISTINCT t) as teacherCount, " +
           "COUNT(DISTINCT g) as groupCount " +
           "FROM Branch b " +
           "LEFT JOIN b.students s " +
           "LEFT JOIN b.teachers t " +
           "LEFT JOIN b.groups g " +
           "WHERE b.active = true " +
           "GROUP BY b.id, b.name")
    List<Object[]> getBranchStatistics();
    
    // Find branches ordered by name
    List<Branch> findAllByOrderByNameAsc();
}