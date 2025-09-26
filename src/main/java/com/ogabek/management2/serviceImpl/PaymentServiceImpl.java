package com.ogabek.management2.serviceImpl;

import com.ogabek.management2.dto.*;
import com.ogabek.management2.dto.payment.CreatePaymentRequest;
import com.ogabek.management2.dto.payment.PaymentHistoryDto;
import com.ogabek.management2.dto.payment.ProcessPaymentRequest;
import com.ogabek.management2.entity.*;
import com.ogabek.management2.exception.ApiException;
import com.ogabek.management2.exception.ResourceNotFoundException;
import com.ogabek.management2.mapper.PaymentMapper;
import com.ogabek.management2.repository.*;
import com.ogabek.management2.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final StudentRepository studentRepository;
    private final LessonRepository lessonRepository;
    private final AttendanceRepository attendanceRepository;

    @Override
    public PaymentHistoryDto processPayment(ProcessPaymentRequest request) {
        Student student = studentRepository.findByUsername(request.getStudentUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + request.getStudentUsername()));

        // Update student balance
        student.setBalance(student.getBalance().add(request.getAmount()));
        studentRepository.save(student);

        // Create payment record
        Payment payment = PaymentMapper.toEntity(request, student);
        paymentRepository.save(payment);

        return PaymentMapper.toDto(payment);
    }

    @Override
    public PaymentHistoryDto createPayment(CreatePaymentRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        // Update student balance
        student.setBalance(student.getBalance().add(request.getAmount()));
        studentRepository.save(student);

        // Create payment record
        Payment payment = PaymentMapper.toEntity(request, student);
        paymentRepository.save(payment);

        return PaymentMapper.toDto(payment);
    }

    @Override
    public void chargeLessonFee(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        if (lesson.isCharged()) {
            throw new ApiException("Lesson has already been charged");
        }

        for (Student student : lesson.getGroup().getStudents()) {
            // Check attendance
            boolean shouldCharge = attendanceRepository
                    .findByLessonIdAndStudentId(lessonId, student.getId())
                    .map(attendance -> attendance.getStatus() != AttendanceStatus.EXCUSED_ABSENT)
                    .orElse(true); // If no attendance record, still charge

            if (shouldCharge) {
                BigDecimal lessonFee = lesson.getGroup().getPricePerLesson();
                
                // Deduct from balance
                student.setBalance(student.getBalance().subtract(lessonFee));
                studentRepository.save(student);

                // Create payment record
                Payment payment = Payment.builder()
                        .student(student)
                        .amount(lessonFee.negate())
                        .type(PaymentType.TUITION)
                        .description("Lesson fee: " + lesson.getTitle())
                        .build();
                paymentRepository.save(payment);
            }
        }

        lesson.setCharged(true);
        lessonRepository.save(lesson);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentHistoryDto> getPaymentHistory(String studentUsername) {
        return paymentRepository.findByStudentUsernameOrderByCreatedAtDesc(studentUsername)
                .stream()
                .map(PaymentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentHistoryDto> getPaymentsByStudent(Long studentId) {
        return paymentRepository.findByStudentIdOrderByCreatedAtDesc(studentId)
                .stream()
                .map(PaymentMapper::toDto)
                .collect(Collectors.toList());
    }

//    @Override
//    @Transactional(readOnly = true)
//    public List<PaymentHistoryDto> getPaymentsByBranch(Long branchId, LocalDate startDate, LocalDate endDate) {
//        LocalDateTime start = startDate.atStartOfDay();
//        LocalDateTime end = endDate.atTime(23, 59, 59);
//
//        return paymentRepository.findByStudentBranchIdAndCreatedAtBetween(branchId, start, end)
//                .stream()
//                .map(PaymentMapper::toDto)
//                .collect(Collectors.toList());
//    }

//    @Override
//    @Transactional(readOnly = true)
//    public BigDecimal getTotalPaymentsByBranch(Long branchId, LocalDate startDate, LocalDate endDate) {
//        LocalDateTime start = startDate.atStartOfDay();
//        LocalDateTime end = endDate.atTime(23, 59, 59);
//
//        return paymentRepository.getTotalPaymentsByBranchAndDateRange(branchId, start, end);
//    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getStudentBalance(String studentUsername) {
        Student student = studentRepository.findByUsername(studentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentUsername));
        return student.getBalance();
    }
}