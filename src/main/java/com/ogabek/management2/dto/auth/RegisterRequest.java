package com.ogabek.management2.dto.auth;


import com.ogabek.management2.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RegisterRequest {

    @NotBlank
    @Size(min = 3, max = 100)
    private String username;
    @NotBlank
    @Size(min = 6, max = 100)
    private String password;

    @NotNull
    private Role role;
}
