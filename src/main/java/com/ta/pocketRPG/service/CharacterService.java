package com.ta.pocketRPG.service;

import com.ta.pocketRPG.domain.dto.CharacterRequest;
import com.ta.pocketRPG.domain.model.City;
import com.ta.pocketRPG.domain.model.GameCharacter;
import com.ta.pocketRPG.domain.model.User;
import com.ta.pocketRPG.repository.CharacterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CharacterService {
    @Autowired
    private CharacterRepository characterRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CityService cityService;

    public GameCharacter createCharacter(CharacterRequest characterRequest) {
        GameCharacter gameCharacter = new GameCharacter();
        gameCharacter.setCharacterName(characterRequest.getCharacterName());
        gameCharacter.setStr(characterRequest.getStr());
        gameCharacter.setAgi(characterRequest.getAgi());
        gameCharacter.setInte(characterRequest.getInte());
        gameCharacter.setGold(characterRequest.getGold());
        gameCharacter.setRes(characterRequest.getRes());
        gameCharacter.setUser(userService.getCurrentUser());

        User currentUser = userService.getCurrentUser();
        gameCharacter.setUser(currentUser);

        gameCharacter.setCity(cityService.getById(1L));


        return characterRepository.save(gameCharacter);
    }

    public List<GameCharacter> getAllCharacters() {
        return characterRepository.findAll();
    }

    public List<GameCharacter> charactersWithEnemies() {
        return characterRepository.findByEnemyIdNot(0);
    }
    public void saveAll(List<GameCharacter> list){
        characterRepository.saveAll(list);
    }

}
