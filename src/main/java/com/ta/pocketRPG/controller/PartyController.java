package com.ta.pocketRPG.controller;

import com.ta.pocketRPG.domain.model.City;
import com.ta.pocketRPG.domain.model.GameCharacter;
import com.ta.pocketRPG.domain.model.Party;
import com.ta.pocketRPG.domain.model.User;
import com.ta.pocketRPG.repository.CharacterRepository;
import com.ta.pocketRPG.repository.CityRepository;
import com.ta.pocketRPG.repository.PartyRepository;
import com.ta.pocketRPG.repository.UserRepository;
import com.ta.pocketRPG.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/party")
public class PartyController {
    private final PartyRepository partyRepository;
    private final UserService userService;
    private final CharacterRepository characterRepository;
    private final CityRepository cityRepository;

    public PartyController(PartyRepository partyRepository, UserService userService, CharacterRepository characterRepository, CityRepository cityRepository) {
        this.partyRepository = partyRepository;
        this.userService = userService;
        this.characterRepository = characterRepository;
        this.cityRepository = cityRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createParty(){
        User user = userService.getCurrentUser();
        GameCharacter gameCharacter = characterRepository.getById(user.getSelectedCharacterId());

        Optional<City> city = cityRepository.findById(gameCharacter.getCity().getId());
        if (city.isEmpty()) {
            return ResponseEntity.badRequest().body("City not found.");
        }

        Party party = new Party();
        party.setPartyName(gameCharacter.getCharacterName());
        gameCharacter.setParty(party); //add the character to new party

        partyRepository.save(party); // Save the party (this will also save the GameCharacter association due to cascade settings)

        List<GameCharacter> characters = new ArrayList<>();


//        gameCharacter = characterRepository.getById(user.getSelectedCharacterId()+1); //delete this code
//        gameCharacter.setParty(party);
//        partyRepository.save(party);

        return ResponseEntity.ok("Party created");
    }
}
