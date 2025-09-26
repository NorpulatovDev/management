package com.ogabek.management2.dto.group;

import com.ogabek.management2.entity.GroupStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UpdateGroupRequest {
    private String name;
    private String description;
    private BigDecimal pricePerLesson;
    private GroupStatus status;
    private Long teacherId;
}