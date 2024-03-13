package com.ta.pocketRPG.repository;

import com.ta.pocketRPG.domain.model.GameCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRepository extends JpaRepository<GameCharacter, Long> {
    GameCharacter save(GameCharacter gameCharacter);
}
