package com.ogabek.management2.service;

import com.ogabek.management2.entity.User;

public interface UserService {
    User findByUsername(String username);
}
