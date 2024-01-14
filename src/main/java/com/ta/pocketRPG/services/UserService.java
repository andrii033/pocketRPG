package com.ta.pocketRPG.services;

import com.ta.pocketRPG.model.User;
import com.ta.pocketRPG.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    public User registerUser(String username, String password) {
        System.out.println("registerUser");
        User newUser = new User();
        newUser.setUsername("name");
        newUser.setPassword("12345");
        newUser.setEmail("user@user");
        newUser.setUsername(username);
        // Store the password in plaintext (not recommended for production)
        newUser.setPassword(password);
        return userRepository.save(newUser);
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username) != null;
    }
}