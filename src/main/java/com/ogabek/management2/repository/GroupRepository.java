package com.ogabek.management2.repository;

import com.ogabek.management2.entity.Group;
import com.ogabek.management2.entity.GroupStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByStatus(GroupStatus status);
    List<Group> findByBranchId(Long branchId);
    List<Group> findByTeacherId(Long teacherId);

    @Query("select g from Group g where g.teacher.user.username = :teacherUsername")
    List<Group> findByTeacherUsername(String teacherUsername);

    @Query("SELECT g FROM Group g WHERE g.branch.id = :branchId AND g.status = :status")
    List<Group> findByBranchIdAndStatus(@Param("branchId") Long branchId, @Param("status") GroupStatus status);

    @Query("SELECT g FROM Group g WHERE g.teacher.id = :teacherId AND g.status = 'ACTIVE'")
    List<Group> findActiveGroupsByTeacherId(@Param("teacherId") Long teacherId);

    // Get group statistics
    @Query("SELECT g.id, g.name, COUNT(s) as studentCount " +
            "FROM Group g LEFT JOIN g.students s " +
            "WHERE g.branch.id = :branchId " +
            "GROUP BY g.id, g.name")
    List<Object[]> getGroupStatisticsByBranch(@Param("branchId") Long branchId);

    Long countByBranchId(Long branchId);
}
