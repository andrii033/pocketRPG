package com.ta.pocketRPG.service;

import com.ta.pocketRPG.domain.model.Role;
import com.ta.pocketRPG.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.ta.pocketRPG.domain.model.User;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    /**
     * User protection
     * @return the stored user
     */
    public User save(User user) {
        return repository.save(user);
    }


    /**
     * User creation
     * @return the created user
     */
    public User create(User user) {
//        if (repository.existsByUsername(user.getUsername())) {
//            throw new RuntimeException("A user with that name already exists");
//        }
//
//        if (repository.existsByEmail(user.getEmail())) {
//            throw new RuntimeException("A user with this email already exists");
//        }

        return save(user);
    }

    /**
     * Getting the user by the user's name
     * @return the user
     */
    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    }

    /**
     * Getting the user by the user's name
     * Required for Spring Security
     * @return the user
     */
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    /**
     * Getting the current user
     * @return the current user
     */
    public User getCurrentUser() {
// Getting the username from the Spring Security context
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }



    /**
     * Issuance of administrator rights to the current user
     * Needed for demonstration
     */
    @Deprecated
    public void getAdmin() {
//        var user = getCurrentUser();
//        user.setRole(Role.ROLE_ADMIN);
//        save(user);
    }
}
