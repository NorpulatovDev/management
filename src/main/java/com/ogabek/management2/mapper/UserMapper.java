package com.ogabek.management2.mapper;

import com.ogabek.management2.dto.UserDto;
import com.ogabek.management2.entity.User;

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
}
