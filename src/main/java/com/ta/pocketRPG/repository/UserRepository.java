package com.ta.pocketRPG.repository;

import com.ta.pocketRPG.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
