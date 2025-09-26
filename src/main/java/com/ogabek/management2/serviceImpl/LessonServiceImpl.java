package com.ogabek.management2.serviceImpl;

import com.ogabek.management2.dto.*;
import com.ogabek.management2.dto.lesson.CreateLessonRequest;
import com.ogabek.management2.dto.lesson.LessonDto;
import com.ogabek.management2.dto.lesson.UpdateLessonRequest;
import com.ogabek.management2.entity.*;
import com.ogabek.management2.exception.ApiException;
import com.ogabek.management2.exception.ResourceNotFoundException;
import com.ogabek.management2.mapper.LessonMapper;
import com.ogabek.management2.repository.*;
import com.ogabek.management2.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final GroupRepository groupRepository;
    private final TeacherRepository teacherRepository;
    private final ScheduleRepository scheduleRepository;
    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public void generateMonthlyLessons(LocalDate month) {
        LocalDate startOfMonth = month.withDayOfMonth(1);
        LocalDate endOfMonth = month.withDayOfMonth(month.lengthOfMonth());
        
        List<Group> activeGroups = groupRepository.findByStatus(GroupStatus.ACTIVE);
        
        for (Group group : activeGroups) {
            List<Schedule> schedules = scheduleRepository.findByGroupIdAndActive(group.getId(), true);
            
            for (Schedule schedule : schedules) {
                generateLessonsForSchedule(group, schedule, startOfMonth, endOfMonth);
            }
        }
    }
    
    private void generateLessonsForSchedule(Group group, Schedule schedule, LocalDate startDate, LocalDate endDate) {
        LocalDate currentDate = startDate;
        
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
                }
            }
            currentDate = currentDate.plusDays(1);
        }
    }

    @Override
    public LessonDto createLesson(CreateLessonRequest request, String teacherUsername) {
        // Find teacher
        Teacher teacher = teacherRepository.findByUsername(teacherUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        // Find group and verify teacher owns it
        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        if (!group.getTeacher().getId().equals(teacher.getId())) {
            throw new ApiException("You can only create lessons for your own groups");
        }

        // Create lesson
        Lesson lesson = LessonMapper.toEntity(request, group, teacher);
        lessonRepository.save(lesson);

        return LessonMapper.toDto(lesson);
    }

    @Override
    public LessonDto updateLesson(Long id, UpdateLessonRequest request) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        LessonMapper.updateEntity(lesson, request);
        lessonRepository.save(lesson);

        return LessonMapper.toDto(lesson);
    }

    @Override
    public LessonDto updateLessonStatus(Long lessonId, LessonStatus status, String username) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        lesson.setStatus(status);
        lessonRepository.save(lesson);

        // If lesson is completed and not yet charged, charge students
        if (status == LessonStatus.COMPLETED && !lesson.isCharged()) {
            chargeStudentsForLesson(lesson);
            lesson.setCharged(true);
            lessonRepository.save(lesson);
        }

        return LessonMapper.toDto(lesson);
    }

    private void chargeStudentsForLesson(Lesson lesson) {
        for (Student student : lesson.getGroup().getStudents()) {
            // Get attendance for this student in this lesson
            Optional<Attendance> attendance = attendanceRepository
                    .findByLessonIdAndStudentId(lesson.getId(), student.getId());

            boolean shouldCharge = true;

            if (attendance.isPresent()) {
                AttendanceStatus status = attendance.get().getStatus();
                if (status == AttendanceStatus.EXCUSED_ABSENT) {
                    shouldCharge = false;
                }
            }

            if (shouldCharge) {
                // Update student balance
                BigDecimal lessonFee = lesson.getGroup().getPricePerLesson();
                student.setBalance(student.getBalance().subtract(lessonFee));
                studentRepository.save(student);

                // Create payment record (negative amount = charge)
                Payment payment = Payment.builder()
                        .student(student)
                        .amount(lessonFee.negate())
                        .type(PaymentType.TUITION)
                        .description("Lesson fee: " + lesson.getTitle())
                        .build();
                paymentRepository.save(payment);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public LessonDto findById(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        return LessonMapper.toDto(lesson);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonDto> findAll() {
        return lessonRepository.findAll()
                .stream()
                .map(LessonMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonDto> findByGroup(Long groupId) {
        return lessonRepository.findByGroupIdAndLessonDateBetween(
                        groupId, 
                        LocalDate.now().withDayOfMonth(1), 
                        LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()))
                .stream()
                .map(LessonMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonDto> findByTeacher(String teacherUsername, LocalDate startDate, LocalDate endDate) {
        return lessonRepository.findByTeacherUsernameAndDateBetween(teacherUsername, startDate, endDate)
                .stream()
                .map(LessonMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonDto> findByBranch(Long branchId) {
        return lessonRepository.findByBranchId(branchId)
                .stream()
                .map(LessonMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteLesson(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        
        if (lesson.isCharged()) {
            throw new ApiException("Cannot delete lesson that has been charged");
        }
        
        lessonRepository.delete(lesson);
    }
}