package com.ogabek.management2.repository;

import com.ogabek.management2.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByGroupIdAndActive(Long groupId, boolean active);
    List<Schedule> findByGroupId(Long groupId);
}