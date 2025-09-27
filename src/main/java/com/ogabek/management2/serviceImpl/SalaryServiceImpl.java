package com.ogabek.management2.serviceImpl;

import com.ogabek.management2.dto.salary.CreateSalaryRequest;
import com.ogabek.management2.dto.salary.SalaryDto;
import com.ogabek.management2.dto.salary.UpdateSalaryRequest;
import com.ogabek.management2.entity.*;
import com.ogabek.management2.exception.ApiException;
import com.ogabek.management2.exception.ResourceNotFoundException;
import com.ogabek.management2.mapper.SalaryMapper;
import com.ogabek.management2.repository.*;
import com.ogabek.management2.service.SalaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SalaryServiceImpl implements SalaryService {

    private final SalaryRepository salaryRepository;
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public SalaryDto createSalary(CreateSalaryRequest request, String processedByUsername) {
        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        User processedBy = userRepository.findByUsername(processedByUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Check if salary already exists for this teacher and month
        if (salaryRepository.existsByTeacherIdAndSalaryMonth(request.getTeacherId(), request.getSalaryMonth())) {
            throw new ApiException("Salary already exists for this teacher and month");
        }

        Salary salary = SalaryMapper.toEntity(request, teacher, processedBy);
        salaryRepository.save(salary);

        return SalaryMapper.toDto(salary);
    }

    @Override
    public SalaryDto updateSalary(Long id, UpdateSalaryRequest request) {
        Salary salary = salaryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salary not found"));

        if (salary.getStatus() == SalaryStatus.PAID) {
            throw new ApiException("Cannot update paid salary");
        }

        SalaryMapper.updateEntity(salary, request);
        salaryRepository.save(salary);

        return SalaryMapper.toDto(salary);
    }

    @Override
    @Transactional(readOnly = true)
    public SalaryDto findById(Long id) {
        Salary salary = salaryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salary not found"));
        return SalaryMapper.toDto(salary);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalaryDto> findByTeacher(Long teacherId) {
        return salaryRepository.findByTeacherId(teacherId)
                .stream()
                .map(SalaryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalaryDto> findByBranch(Long branchId) {
        return salaryRepository.findByTeacherBranchId(branchId)
                .stream()
                .map(SalaryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalaryDto> findByMonth(LocalDate month) {
        return salaryRepository.findBySalaryMonth(month)
                .stream()
                .map(SalaryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalaryDto> findPendingSalaries() {
        return salaryRepository.findPendingSalaries()
                .stream()
                .map(SalaryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public SalaryDto calculateAndCreateMonthlySalary(Long teacherId, LocalDate month, String processedByUsername) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        User processedBy = userRepository.findByUsername(processedByUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Check if salary already exists
        if (salaryRepository.existsByTeacherIdAndSalaryMonth(teacherId, month)) {
            throw new ApiException("Salary already exists for this teacher and month");
        }

        // Calculate completed lessons for the month
        LocalDate startOfMonth = month.withDayOfMonth(1);
        LocalDate endOfMonth = month.withDayOfMonth(month.lengthOfMonth());
        
        List<Lesson> completedLessons = lessonRepository.findByTeacherUsernameAndDateBetween(
                teacher.getUser().getUsername(), startOfMonth, endOfMonth)
                .stream()
                .filter(lesson -> lesson.getStatus() == LessonStatus.COMPLETED)
                .collect(Collectors.toList());

        int totalLessons = completedLessons.size();
        
        // Calculate total revenue from teacher's lessons
        BigDecimal totalRevenue = completedLessons.stream()
                .map(lesson -> lesson.getGroup().getPricePerLesson()
                        .multiply(BigDecimal.valueOf(lesson.getGroup().getStudents().size())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculate salary amount
        BigDecimal percentage = teacher.getSalaryPercentage();
        BigDecimal salaryAmount = totalRevenue
                .multiply(percentage)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        // Create salary record
        Salary salary = Salary.builder()
                .teacher(teacher)
                .amount(salaryAmount)
                .salaryMonth(month)
                .totalLessons(totalLessons)
                .percentage(percentage)
                .status(SalaryStatus.PENDING)
                .processedBy(processedBy)
                .build();

        salaryRepository.save(salary);
        return SalaryMapper.toDto(salary);
    }

    @Override
    public SalaryDto markSalaryAsPaid(Long salaryId, String processedByUsername) {
        Salary salary = salaryRepository.findById(salaryId)
                .orElseThrow(() -> new ResourceNotFoundException("Salary not found"));

        User processedBy = userRepository.findByUsername(processedByUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (salary.getStatus() == SalaryStatus.PAID) {
            throw new ApiException("Salary is already paid");
        }

        salary.setStatus(SalaryStatus.PAID);
        salary.setProcessedBy(processedBy);
        salaryRepository.save(salary);

        return SalaryMapper.toDto(salary);
    }

    @Override
    public void deleteSalary(Long id) {
        Salary salary = salaryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salary not found"));

        if (salary.getStatus() == SalaryStatus.PAID) {
            throw new ApiException("Cannot delete paid salary");
        }

        salaryRepository.delete(salary);
    }
}