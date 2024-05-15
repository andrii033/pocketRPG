package com.ta.pocketRPG.service;

import com.ta.pocketRPG.domain.dto.CharacterRequest;
import com.ta.pocketRPG.domain.model.GameCharacter;
import com.ta.pocketRPG.domain.model.User;
import com.ta.pocketRPG.repository.CharacterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CharacterService {
    @Autowired
    private CharacterRepository characterRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CityService cityService;

    public void createCharacter(CharacterRequest characterRequest) {
        GameCharacter gameCharacter = new GameCharacter();
        gameCharacter.setCharacterName(characterRequest.getCharacterName());
        gameCharacter.setStr(1);
        gameCharacter.setAgi(1);
        gameCharacter.setInte(1);
        gameCharacter.setGold(1);
        gameCharacter.setRes(0);
        gameCharacter.setExp(0);
        gameCharacter.setMainPoints(0);
        gameCharacter.setSecondaryPoints(0);
        gameCharacter.setUser(userService.getCurrentUser());
        //gameCharacter.setEnemyId(1);

        User currentUser = userService.getCurrentUser();
        gameCharacter.setUser(currentUser);

        gameCharacter.setCity(cityService.getById(1L));


        characterRepository.save(gameCharacter);
    }


    public List<GameCharacter> charactersWithEnemies() {
        return characterRepository.findByEnemyIdNot(0);
    }
    public void saveAll(List<GameCharacter> list){
        characterRepository.saveAll(list);
    }

}
