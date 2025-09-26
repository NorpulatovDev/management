package com.ogabek.management2.dto.group;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor
public class AddStudentsToGroupRequest {
    @NotNull(message = "Student IDs are required")
    @NotEmpty(message = "At least one student ID is required")
    private List<Long> studentIds;
}