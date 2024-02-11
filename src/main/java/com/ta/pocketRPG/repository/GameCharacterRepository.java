package com.ta.pocketRPG.repository;

import com.ta.pocketRPG.model.GameCharacter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameCharacterRepository extends JpaRepository<GameCharacter, Long> {
    List<GameCharacter> findByUserUsername(String username);
}
