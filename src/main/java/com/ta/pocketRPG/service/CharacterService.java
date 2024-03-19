package com.ta.pocketRPG.service;

import com.ta.pocketRPG.domain.dto.CharacterRequest;
import com.ta.pocketRPG.domain.model.GameCharacter;
import com.ta.pocketRPG.domain.model.User;
import com.ta.pocketRPG.repository.CharacterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CharacterService {
    @Autowired
    private CharacterRepository characterRepository;
    @Autowired
    private UserService userService;

    public GameCharacter createCharacter(CharacterRequest characterRequest){
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

        return characterRepository.save(gameCharacter);
    }
}
