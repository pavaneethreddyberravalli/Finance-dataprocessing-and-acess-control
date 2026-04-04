package com.finance.service;

import com.finance.entity.User;
import com.finance.excption.UserNotFoundException;
import com.finance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User getEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
