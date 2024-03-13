package com.ta.pocketRPG.controller;

import com.ta.pocketRPG.domain.dto.CharacterRequest;
import com.ta.pocketRPG.domain.model.GameCharacter;
import com.ta.pocketRPG.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/character")
public class CharacterController {

    @Autowired
    private CharacterService characterService;

    @PostMapping("/create")
    public String createCharacter(@RequestBody CharacterRequest characterRequest){
        //return characterService.createCharacter(characterRequest);
        return "create";
    }

}
