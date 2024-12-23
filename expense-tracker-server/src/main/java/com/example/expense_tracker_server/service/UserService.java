package com.example.expense_tracker_server.service;

import com.example.expense_tracker_server.controller.UserController;
import com.example.expense_tracker_server.entity.User;
import com.example.expense_tracker_server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class UserService {
    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    @Autowired
    private UserRepository userRepository;

    public User createUser(String name, String email, String password){
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public Optional<User> getUserByEmail(String email){
        logger.info("Getting user by email in repository");
        return userRepository.findByEmail(email);
    }

    public void deleteUserByEmail(String email){
        userRepository.deleteByEmail(email);
    }
}













