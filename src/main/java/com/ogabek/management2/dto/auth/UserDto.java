package com.ogabek.management2.dto.auth;


import com.ogabek.management2.entity.Role;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserDto {
    private Long id;
    private String username;
    private Role role;
    private boolean enabled;
}
