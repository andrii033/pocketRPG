package com.ta.pocketRPG.services;

import com.ta.pocketRPG.model.GameCharacter;
import com.ta.pocketRPG.model.User;
import com.ta.pocketRPG.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(String username, String password, String email) {

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setEmail(email);

        return userRepository.save(newUser);
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void saveGameCharacter(String username, GameCharacter character) {
        User user = userRepository.findByUsername(username);

        if (user.getCharacters() == null) {
            user.setCharacters(new ArrayList<>());
        }

        character.setUser(user);
        user.getCharacters().add(character);

        userRepository.save(user);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return (UserDetails) user;
    }

    public GameCharacter loadGameCharacterByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        if (user.getCharacters() == null || user.getCharacters().isEmpty()) {
            // No GameCharacter found for the given username
            return null;
        }

        // For simplicity, assuming that a user has only one GameCharacter.
        // Adjust this logic based on your actual requirements.
        return user.getCharacters().get(0);
    }
}