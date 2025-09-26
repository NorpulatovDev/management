package com.ogabek.management2.service;

import com.ogabek.management2.dto.*;
import com.ogabek.management2.dto.salary.CreateSalaryRequest;
import com.ogabek.management2.dto.salary.SalaryDto;
import com.ogabek.management2.dto.salary.UpdateSalaryRequest;
import com.ogabek.management2.entity.SalaryStatus;
import java.time.LocalDate;
import java.util.List;

public interface SalaryService {
    SalaryDto createSalary(CreateSalaryRequest request, String processedByUsername);
    SalaryDto updateSalary(Long id, UpdateSalaryRequest request);
    SalaryDto findById(Long id);
    List<SalaryDto> findByTeacher(Long teacherId);
    List<SalaryDto> findByBranch(Long branchId);
    List<SalaryDto> findByMonth(LocalDate month);
    List<SalaryDto> findPendingSalaries();
    SalaryDto calculateAndCreateMonthlySalary(Long teacherId, LocalDate month, String processedByUsername);
    SalaryDto markSalaryAsPaid(Long salaryId, String processedByUsername);
    void deleteSalary(Long id);
}