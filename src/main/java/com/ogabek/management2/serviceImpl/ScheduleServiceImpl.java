package com.ogabek.management2.serviceImpl;

import com.ogabek.management2.dto.*;
import com.ogabek.management2.dto.schedule.CreateScheduleRequest;
import com.ogabek.management2.dto.schedule.ScheduleDto;
import com.ogabek.management2.dto.schedule.UpdateScheduleRequest;
import com.ogabek.management2.entity.*;
import com.ogabek.management2.exception.ApiException;
import com.ogabek.management2.exception.ResourceNotFoundException;
import com.ogabek.management2.mapper.ScheduleMapper;
import com.ogabek.management2.repository.*;
import com.ogabek.management2.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final GroupRepository groupRepository;

    @Override
    public ScheduleDto createSchedule(CreateScheduleRequest request, Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        // Check for time conflicts within the same group
        List<Schedule> existingSchedules = scheduleRepository.findByGroupIdAndActive(groupId, true);
        for (Schedule existing : existingSchedules) {
            if (existing.getDayOfWeek() == request.getDayOfWeek()) {
                if (isTimeOverlapping(existing.getStartTime(), existing.getEndTime(), 
                                    request.getStartTime(), request.getEndTime())) {
                    throw new ApiException("Schedule conflicts with existing schedule on " + request.getDayOfWeek());
                }
            }
        }

        Schedule schedule = ScheduleMapper.toEntity(request, group);
        scheduleRepository.save(schedule);

        return ScheduleMapper.toDto(schedule);
    }

    @Override
    public ScheduleDto updateSchedule(Long id, UpdateScheduleRequest request) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));

        // Check for conflicts if day or time is being changed
        if (request.getDayOfWeek() != null || request.getStartTime() != null || request.getEndTime() != null) {
            List<Schedule> existingSchedules = scheduleRepository.findByGroupIdAndActive(
                schedule.getGroup().getId(), true);
            
            for (Schedule existing : existingSchedules) {
                if (!existing.getId().equals(id) && 
                    existing.getDayOfWeek() == (request.getDayOfWeek() != null ? request.getDayOfWeek() : schedule.getDayOfWeek())) {
                    
                    if (isTimeOverlapping(
                        existing.getStartTime(), existing.getEndTime(),
                        request.getStartTime() != null ? request.getStartTime() : schedule.getStartTime(),
                        request.getEndTime() != null ? request.getEndTime() : schedule.getEndTime())) {
                        throw new ApiException("Schedule conflicts with existing schedule");
                    }
                }
            }
        }

        ScheduleMapper.updateEntity(schedule, request);
        scheduleRepository.save(schedule);

        return ScheduleMapper.toDto(schedule);
    }

    @Override
    @Transactional(readOnly = true)
    public ScheduleDto findById(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));
        return ScheduleMapper.toDto(schedule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScheduleDto> findByGroup(Long groupId) {
        return scheduleRepository.findByGroupId(groupId)
                .stream()
                .map(ScheduleMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScheduleDto> findActiveByGroup(Long groupId) {
        return scheduleRepository.findByGroupIdAndActive(groupId, true)
                .stream()
                .map(ScheduleMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteSchedule(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));
        
        scheduleRepository.delete(schedule);
    }

    @Override
    public void deactivateSchedule(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));
        
        schedule.setActive(false);
        scheduleRepository.save(schedule);
    }

    @Override
    public void activateSchedule(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));
        
        schedule.setActive(true);
        scheduleRepository.save(schedule);
    }

    private boolean isTimeOverlapping(java.time.LocalTime start1, java.time.LocalTime end1,
                                     java.time.LocalTime start2, java.time.LocalTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }
}