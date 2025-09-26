package com.ogabek.management2.service;

import com.ogabek.management2.dto.student.StudentDto;
import com.ogabek.management2.dto.student.CreateStudentRequest;
import com.ogabek.management2.dto.student.UpdateStudentRequest;
import com.ogabek.management2.dto.student.AddBalanceRequest;
import com.ogabek.management2.dto.payment.PaymentHistoryDto;
import java.math.BigDecimal;
import java.util.List;

public interface StudentService {
    StudentDto createStudent(CreateStudentRequest request);
    StudentDto updateStudent(Long id, UpdateStudentRequest request);
    StudentDto findById(Long id);
    StudentDto findByUsername(String username);
    List<StudentDto> findAll();
    List<StudentDto> findByBranch(Long branchId);
    List<StudentDto> findByGroup(Long groupId);
    void deleteStudent(Long id);
    BigDecimal getBalance(String username);
    StudentDto addBalance(String username, AddBalanceRequest request);
    List<PaymentHistoryDto> getPaymentHistory(String username);
}