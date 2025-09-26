package com.ogabek.management2.dto.student;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UpdateStudentRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String parentPhoneNumber;
    private Long branchId;
}