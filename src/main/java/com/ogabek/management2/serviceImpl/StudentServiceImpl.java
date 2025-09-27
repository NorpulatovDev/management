package com.ogabek.management2.serviceImpl;

import com.ogabek.management2.dto.payment.PaymentHistoryDto;
import com.ogabek.management2.dto.student.AddBalanceRequest;
import com.ogabek.management2.dto.student.CreateStudentRequest;
import com.ogabek.management2.dto.student.StudentDto;
import com.ogabek.management2.dto.student.UpdateStudentRequest;
import com.ogabek.management2.entity.*;
import com.ogabek.management2.exception.ApiException;
import com.ogabek.management2.exception.ResourceNotFoundException;
import com.ogabek.management2.mapper.StudentMapper;
import com.ogabek.management2.mapper.PaymentMapper;
import com.ogabek.management2.repository.*;
import com.ogabek.management2.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public StudentDto createStudent(CreateStudentRequest request) {
        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ApiException("Username already exists: " + request.getUsername());
        }

        // Find branch
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));

        // Create user account
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.STUDENT)
                .enabled(true)
                .build();
        userRepository.save(user);

        // Create student
        Student student = StudentMapper.toEntity(request, branch, user);
        studentRepository.save(student);

        return StudentMapper.toDto(student);
    }

    @Override
    public StudentDto updateStudent(Long id, UpdateStudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        Branch branch = null;
        if (request.getBranchId() != null) {
            branch = branchRepository.findById(request.getBranchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));
        }

        StudentMapper.updateEntity(student, request, branch);
        studentRepository.save(student);

        return StudentMapper.toDto(student);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDto findById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        return StudentMapper.toDto(student);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDto findByUsername(String username) {
        Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + username));
        return StudentMapper.toDto(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDto> findAll() {
        return studentRepository.findAll()
                .stream()
                .map(StudentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDto> findByBranch(Long branchId) {
        return studentRepository.findByBranchId(branchId)
                .stream()
                .map(StudentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDto> findByGroup(Long groupId) {
        return studentRepository.findByGroupId(groupId)
                .stream()
                .map(StudentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        
        // Check if student has any payments or is in any groups
        if (!student.getPayments().isEmpty()) {
            throw new ApiException("Cannot delete student with payment history");
        }
        
        studentRepository.delete(student);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getBalance(String username) {
        Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + username));
        return student.getBalance();
    }

    @Override
    public StudentDto addBalance(String username, AddBalanceRequest request) {
        Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + username));

        // Update balance
        student.setBalance(student.getBalance().add(request.getAmount()));
        studentRepository.save(student);

        // Create payment record
        Payment payment = Payment.builder()
                .student(student)
                .amount(request.getAmount())
                .type(PaymentType.OTHER)
                .description(request.getDescription() != null ? request.getDescription() : "Balance added")
                .build();
        paymentRepository.save(payment);

        return StudentMapper.toDto(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentHistoryDto> getPaymentHistory(String username) {
        return paymentRepository.findByStudentUsernameOrderByCreatedAtDesc(username)
                .stream()
                .map(PaymentMapper::toDto)
                .collect(Collectors.toList());
    }
}