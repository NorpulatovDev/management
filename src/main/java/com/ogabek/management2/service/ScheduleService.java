package com.ogabek.management2.service;

import com.ogabek.management2.dto.*;
import com.ogabek.management2.dto.schedule.CreateScheduleRequest;
import com.ogabek.management2.dto.schedule.ScheduleDto;
import com.ogabek.management2.dto.schedule.UpdateScheduleRequest;

import java.util.List;

public interface ScheduleService {
    ScheduleDto createSchedule(CreateScheduleRequest request, Long groupId);
    ScheduleDto updateSchedule(Long id, UpdateScheduleRequest request);
    ScheduleDto findById(Long id);
    List<ScheduleDto> findByGroup(Long groupId);
    List<ScheduleDto> findActiveByGroup(Long groupId);
    void deleteSchedule(Long id);
    void deactivateSchedule(Long id);
    void activateSchedule(Long id);
}