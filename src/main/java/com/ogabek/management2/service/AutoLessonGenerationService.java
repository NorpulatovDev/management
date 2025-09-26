package com.ogabek.management2.service;

import com.ogabek.management2.entity.*;
import com.ogabek.management2.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AutoLessonGenerationService {
    
    private final LessonRepository lessonRepository;
    private final GroupRepository groupRepository;
    private final ScheduleRepository scheduleRepository;
    
    // Run on the 1st day of each month at 2 AM
    @Scheduled(cron = "0 0 2 1 * ?")
    @Transactional
    public void generateCurrentMonthLessons() {
        LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);
        log.info("Starting automatic lesson generation for month: {}", currentMonth.format(DateTimeFormatter.ofPattern("MMM yyyy")));
        
        generateMonthlyLessons(currentMonth);
        
        log.info("Completed automatic lesson generation for month: {}", currentMonth.format(DateTimeFormatter.ofPattern("MMM yyyy")));
    }
    
    @Transactional
    public void generateMonthlyLessons(LocalDate month) {
        LocalDate startOfMonth = month.withDayOfMonth(1);
        LocalDate endOfMonth = month.withDayOfMonth(month.lengthOfMonth());
        
        List<Group> activeGroups = groupRepository.findByStatus(GroupStatus.ACTIVE);
        log.info("Found {} active groups for lesson generation", activeGroups.size());
        
        int totalLessonsGenerated = 0;
        
        for (Group group : activeGroups) {
            List<Schedule> schedules = scheduleRepository.findByGroupIdAndActive(group.getId(), true);
            
            for (Schedule schedule : schedules) {
                int lessonsGenerated = generateLessonsForSchedule(group, schedule, startOfMonth, endOfMonth);
                totalLessonsGenerated += lessonsGenerated;
            }
        }
        
        log.info("Generated {} lessons for month: {}", totalLessonsGenerated, month.format(DateTimeFormatter.ofPattern("MMM yyyy")));
    }
    
    private int generateLessonsForSchedule(Group group, Schedule schedule, LocalDate startDate, LocalDate endDate) {
        LocalDate currentDate = startDate;
        int lessonsGenerated = 0;
        
        while (!currentDate.isAfter(endDate)) {
            if (currentDate.getDayOfWeek() == schedule.getDayOfWeek()) {
                // Check if lesson already exists
                boolean exists = lessonRepository.existsByGroupIdAndLessonDate(group.getId(), currentDate);
                    
                if (!exists) {
                    Lesson lesson = Lesson.builder()
                        .title(group.getName() + " - " + currentDate.format(DateTimeFormatter.ofPattern("MMM dd")))
                        .group(group)
                        .lessonDate(currentDate)
                        .startTime(schedule.getStartTime())
                        .endTime(schedule.getEndTime())
                        .status(LessonStatus.PLANNED)
                        .charged(false)
                        .createdBy(null) // Auto-generated
                        .build();
                    
                    lessonRepository.save(lesson);
                    lessonsGenerated++;
                    
                    log.debug("Generated lesson for group '{}' on {}", group.getName(), currentDate);
                }
            }
            currentDate = currentDate.plusDays(1);
        }
        
        return lessonsGenerated;
    }
    
    // Manual trigger for specific month
    @Transactional
    public void generateLessonsForMonth(LocalDate month, Long branchId) {
        LocalDate startOfMonth = month.withDayOfMonth(1);
        LocalDate endOfMonth = month.withDayOfMonth(month.lengthOfMonth());
        
        List<Group> groups;
        if (branchId != null) {
            groups = groupRepository.findByBranchIdAndStatus(branchId, GroupStatus.ACTIVE);
        } else {
            groups = groupRepository.findByStatus(GroupStatus.ACTIVE);
        }
        
        for (Group group : groups) {
            List<Schedule> schedules = scheduleRepository.findByGroupIdAndActive(group.getId(), true);
            
            for (Schedule schedule : schedules) {
                generateLessonsForSchedule(group, schedule, startOfMonth, endOfMonth);
            }
        }
    }
}