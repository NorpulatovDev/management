package com.ogabek.management2.dto.branch;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BranchDto {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private boolean active;
}