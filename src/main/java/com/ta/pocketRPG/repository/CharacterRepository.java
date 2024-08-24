package com.ta.pocketRPG.repository;

import com.ta.pocketRPG.domain.model.GameCharacter;
import com.ta.pocketRPG.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRepository extends JpaRepository<GameCharacter, Long> {
    List<GameCharacter> findGameCharacterByUser(User user);
    List<GameCharacter> findByCityId(Long cityId);
    GameCharacter findById(long id);
}
