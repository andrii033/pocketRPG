package com.ta.pocketRPG.repository;

import com.ta.pocketRPG.domain.model.GameCharacter;
import com.ta.pocketRPG.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CharacterRepository extends JpaRepository<GameCharacter, Long> {
    GameCharacter save(GameCharacter gameCharacter);
    List<GameCharacter> findByEnemyIdNot(int enemyId);
    Optional<GameCharacter> findById(Long id);
    GameCharacter findGameCharacterByUser(User user);
}
