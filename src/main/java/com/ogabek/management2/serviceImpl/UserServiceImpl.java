package com.ogabek.management2.serviceImpl;

import com.ogabek.management2.entity.User;
import com.ogabek.management2.exception.ResourceNotFoundException;
import com.ogabek.management2.repository.UserRepository;
import com.ogabek.management2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(()-> new ResourceNotFoundException("User not found: " + username));
    }
}
