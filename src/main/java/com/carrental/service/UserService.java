package com.carrental.service;

import com.carrental.models.User;
import com.carrental.repository.UserRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("The email is in use");
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        if (user.getIsAdmin() == null) {
            user.setIsAdmin(false);
        }
        
        return userRepository.save(user);
    }
    
    public User findByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new RuntimeException("User not found");
        }
    }

}