package com.ogabek.management2.mapper;

import com.ogabek.management2.dto.auth.UserDto;
import com.ogabek.management2.entity.User;
import com.ogabek.management2.entity.Role;

public class UserMapper {

    public static UserDto toDto(User user) {
        if (user == null) return null;
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .build();
    }

    public static User createUser(String username, String password, Role role) {
        return User.builder()
                .username(username)
                .password(password)
                .role(role)
                .enabled(true)
                .build();
    }
}