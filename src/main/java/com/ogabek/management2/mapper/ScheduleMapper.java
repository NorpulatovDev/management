package com.ogabek.management2.mapper;

import com.ogabek.management2.dto.schedule.ScheduleDto;
import com.ogabek.management2.dto.schedule.CreateScheduleRequest;
import com.ogabek.management2.dto.schedule.UpdateScheduleRequest;
import com.ogabek.management2.entity.Schedule;
import com.ogabek.management2.entity.Group;

public class ScheduleMapper {
    
    public static ScheduleDto toDto(Schedule schedule) {
        if (schedule == null) return null;
        return ScheduleDto.builder()
                .id(schedule.getId())
                .dayOfWeek(schedule.getDayOfWeek())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .active(schedule.isActive())
                .build();
    }
    
    public static Schedule toEntity(CreateScheduleRequest request, Group group) {
        if (request == null) return null;
        return Schedule.builder()
                .group(group)
                .dayOfWeek(request.getDayOfWeek())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .active(true)
                .build();
    }
    
    public static void updateEntity(Schedule schedule, UpdateScheduleRequest request) {
        if (request == null || schedule == null) return;
        
        if (request.getDayOfWeek() != null) {
            schedule.setDayOfWeek(request.getDayOfWeek());
        }
        if (request.getStartTime() != null) {
            schedule.setStartTime(request.getStartTime());
        }
        if (request.getEndTime() != null) {
            schedule.setEndTime(request.getEndTime());
        }
        if (request.getActive() != null) {
            schedule.setActive(request.getActive());
        }
    }

}