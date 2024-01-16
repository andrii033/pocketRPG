package com.ta.pocketRPG.services;

import com.ta.pocketRPG.model.User;
import com.ta.pocketRPG.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    public User registerUser(String username, String password) {

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);

        return userRepository.save(newUser);
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return (UserDetails) user;
    }
}