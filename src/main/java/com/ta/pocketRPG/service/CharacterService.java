package com.ta.pocketRPG.service;

import com.ta.pocketRPG.domain.dto.CharacterRequest;
import com.ta.pocketRPG.domain.model.GameCharacter;
import com.ta.pocketRPG.domain.model.User;
import com.ta.pocketRPG.repository.CharacterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CharacterService {
    private final CharacterRepository characterRepository;
    private final UserService userService;
    private final CityService cityService;

    public CharacterService(CharacterRepository characterRepository, UserService userService, CityService cityService) {
        this.characterRepository = characterRepository;
        this.userService = userService;
        this.cityService = cityService;
    }

    public void createCharacter(CharacterRequest characterRequest) {
        GameCharacter gameCharacter = new GameCharacter();
        gameCharacter.setCharacterName(characterRequest.getCharacterName());
        gameCharacter.setStr(1);
        gameCharacter.setAgi(1);
        gameCharacter.setInte(1);
        gameCharacter.setPhysicalHarm(1);
        gameCharacter.setArmorPiercing(1);
        gameCharacter.setReduceBlockDam(1);
        gameCharacter.setMaxHealth(1);
        gameCharacter.setMaxHealth(1);
        gameCharacter.setCritChance(1);
        gameCharacter.setAttackSpeed(1);
        gameCharacter.setAvoidance(1);
        gameCharacter.setBlockChance(1);
        gameCharacter.setMagicDam(1);
        gameCharacter.setMagicCritChance(1);
        gameCharacter.setManaRegen(1);
        gameCharacter.setMaxMana(1);
        gameCharacter.setGold(1);
        gameCharacter.setRes(0);
        gameCharacter.setExp(0);
        gameCharacter.setLvl(1);
        gameCharacter.setHp(10);
        gameCharacter.setUser(userService.getCurrentUser());

        User currentUser = userService.getCurrentUser();
        gameCharacter.setUser(currentUser);

        gameCharacter.setCity(cityService.getById(1L));

        gameCharacter.setWait(true);

        characterRepository.save(gameCharacter);
    }


    public List<GameCharacter> charactersWithEnemies() {
        return characterRepository.findByEnemyIdNot(0);
    }
    public void saveAll(List<GameCharacter> list){
        characterRepository.saveAll(list);
    }

    public List<GameCharacter> getCharactersByCity(Long cityId) {
        return characterRepository.findByCityId(cityId);
    }
}
