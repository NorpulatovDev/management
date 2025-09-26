package com.ogabek.management2.dto.branch;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UpdateBranchRequest {
    private String name;
    private String address;
    private String phone;
    private Boolean active;
}