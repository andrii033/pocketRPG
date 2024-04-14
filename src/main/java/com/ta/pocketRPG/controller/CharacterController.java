package com.ta.pocketRPG.controller;

import com.ta.pocketRPG.component.RequestRateLimiter;
import com.ta.pocketRPG.domain.dto.CharacterMove;
import com.ta.pocketRPG.domain.dto.CharacterRequest;
import com.ta.pocketRPG.domain.dto.CityRequest;
import com.ta.pocketRPG.domain.model.City;
import com.ta.pocketRPG.domain.model.GameCharacter;
import com.ta.pocketRPG.domain.model.ListOfCities;
import com.ta.pocketRPG.domain.model.User;
import com.ta.pocketRPG.repository.CharacterRepository;
import com.ta.pocketRPG.repository.CityRepository;
import com.ta.pocketRPG.service.CharacterService;
import com.ta.pocketRPG.service.EnemyService;
import com.ta.pocketRPG.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/character")
public class CharacterController {

    @Autowired
    private CharacterService characterService;
    @Autowired
    private CharacterRepository characterRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private EnemyService enemyService;
    @Autowired
    private RequestRateLimiter rateLimiter;
    @Autowired
    private CityRepository cityRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createCharacter(@RequestBody CharacterRequest characterRequest) {
        try {
            characterService.createCharacter(characterRequest);
            return ResponseEntity.ok("Character created successfully.");
        } catch (Exception e) {
            String errorMessage = "Failed to create character: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @GetMapping("/choose")
    public ResponseEntity<?> chooseCharacter() {
        List<CharacterRequest> listCharacterRequest = createCharacterRequestList();
        if (!listCharacterRequest.isEmpty()) {
            return ResponseEntity.ok(listCharacterRequest);
        } else {
            return ResponseEntity.notFound().build(); // Character request not found
        }
    }

    @PostMapping("/choose")
    public ResponseEntity<?> chooseCharacter(@RequestBody CharacterRequest characterRequest) {
        System.out.println(characterRequest.getId());
        User user = userService.getCurrentUser();
        user.setSelectedCharacterId(characterRequest.getId());
        userService.save(user);

        GameCharacter gameCharacter = characterRepository.getById(user.getSelectedCharacterId());
        System.out.println("selectedCharacterID "+gameCharacter.toString());
        List<City> cities = cityRepository.findByListOfCitiesId(gameCharacter.getCity().getListOfCities().getId());
        List<CityRequest> cityRequests = new ArrayList<>();
        for (City city : cities) {
            CityRequest cityRequest = new CityRequest();

            cityRequest.setXCoord(city.getXCoord());
            cityRequest.setYCoord(city.getYCoord());
            cityRequest.setTerrainType(city.getTerrainType());

            cityRequests.add(cityRequest);
        }

        return ResponseEntity.ok(cityRequests);
    }

    private List<CharacterRequest> createCharacterRequestList() {
        List<CharacterRequest> listCharacterRequest = new ArrayList<>();
        List<GameCharacter> gameCharacter = characterRepository.findGameCharacterByUser(userService.getCurrentUser());

        if (gameCharacter != null) {
            for (GameCharacter character : gameCharacter) {
                CharacterRequest characterRequest = new CharacterRequest();
                characterRequest.setCharacterName(character.getCharacterName());
                characterRequest.setId(character.getId());
                characterRequest.setStr(character.getStr());
                characterRequest.setRes(character.getRes());
                characterRequest.setInte(character.getInte());
                characterRequest.setGold(character.getGold());
                characterRequest.setAgi(character.getAgi());
                characterRequest.setXCoord(character.getCity().getXCoord());
                characterRequest.setYCoord(character.getCity().getYCoord());
                listCharacterRequest.add(characterRequest);
            }
        }
        return listCharacterRequest;
    }

    @PostMapping("/move")
    private ResponseEntity<?> moveCharacter(@RequestBody CharacterMove characterMove){
        User user = userService.getCurrentUser();
        GameCharacter gameCharacter = characterRepository.getById(user.getSelectedCharacterId());

        log.info("request "+characterMove.getX()+" "+characterMove.getY());
        
        Long listOfCitiesId = gameCharacter.getCity().getListOfCities().getId();
        List<City> listOfCity = cityRepository.findByListOfCitiesId(listOfCitiesId);

        City targetCity = null;
        for(City city:listOfCity){
            if(city.getXCoord() == characterMove.getX() && city.getYCoord() == characterMove.getY()){
                targetCity=city;
                break;
            }
        }
        gameCharacter.setCity(targetCity);
        characterRepository.save(gameCharacter);

        return ResponseEntity.ok(characterMove);
    }



}
