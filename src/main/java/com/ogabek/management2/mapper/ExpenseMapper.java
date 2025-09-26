package com.ogabek.management2.mapper;

import com.ogabek.management2.dto.expense.ExpenseDto;
import com.ogabek.management2.dto.expense.CreateExpenseRequest;
import com.ogabek.management2.dto.expense.UpdateExpenseRequest;
import com.ogabek.management2.entity.Expense;
import com.ogabek.management2.entity.Branch;
import com.ogabek.management2.entity.User;

public class ExpenseMapper {
    
    public static ExpenseDto toDto(Expense expense) {
        if (expense == null) return null;
        return ExpenseDto.builder()
                .id(expense.getId())
                .description(expense.getDescription())
                .amount(expense.getAmount())
                .type(expense.getType())
                .branch(BranchMapper.toDto(expense.getBranch()))
                .createdBy(UserMapper.toDto(expense.getCreatedBy()))
                .createdAt(expense.getCreatedAt())
                .build();
    }
    
    public static Expense toEntity(CreateExpenseRequest request, Branch branch, User createdBy) {
        if (request == null) return null;
        return Expense.builder()
                .description(request.getDescription())
                .amount(request.getAmount())
                .type(request.getType())
                .branch(branch)
                .createdBy(createdBy)
                .build();
    }
    
    public static void updateEntity(Expense expense, UpdateExpenseRequest request) {
        if (request == null || expense == null) return;
        
        if (request.getDescription() != null) {
            expense.setDescription(request.getDescription());
        }
        if (request.getAmount() != null) {
            expense.setAmount(request.getAmount());
        }
        if (request.getType() != null) {
            expense.setType(request.getType());
        }
    }
}