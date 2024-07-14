package com.ta.pocketRPG.controller;

import com.ta.pocketRPG.domain.dto.*;
import com.ta.pocketRPG.domain.model.*;
import com.ta.pocketRPG.repository.CharacterRepository;
import com.ta.pocketRPG.repository.CityRepository;
import com.ta.pocketRPG.repository.EnemyRepository;
import com.ta.pocketRPG.service.CharacterService;
import com.ta.pocketRPG.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/character")
public class CharacterController {

    private final CharacterService characterService;
    private final CharacterRepository characterRepository;
    private final UserService userService;
    private final CityRepository cityRepository;
    private final EnemyRepository enemyRepository;

    public CharacterController(CharacterService characterService, CharacterRepository characterRepository, UserService userService, CityRepository cityRepository, EnemyRepository enemyRepository) {
        this.characterService = characterService;
        this.characterRepository = characterRepository;
        this.userService = userService;
        this.cityRepository = cityRepository;
        this.enemyRepository = enemyRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCharacter(@RequestBody CharacterRequest characterRequest) {
        System.out.println("createCharacter");
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
    public ResponseEntity<?> chooseCharacter(@RequestBody String id) {
        System.out.println(id);
        User user = userService.getCurrentUser();
        user.setSelectedCharacterId(Long.valueOf(id));
        userService.save(user);

        GameCharacter gameCharacter = characterRepository.getById(user.getSelectedCharacterId());
        //System.out.println("selectedCharacterID " + gameCharacter);
        List<City> cities = cityRepository.findByListOfCitiesId(gameCharacter.getCity().getListOfCities().getId());
        List<CityRequest> cityRequests = getCityRequests(cities);

        return ResponseEntity.ok(cityRequests);
    }

    private static List<CityRequest> getCityRequests(List<City> cities) {
        List<CityRequest> cityRequests = new ArrayList<>();
        for (City city : cities) {
            CityRequest cityRequest = new CityRequest();

            cityRequest.setXCoord(city.getXCoord());
            cityRequest.setYCoord(city.getYCoord());
            cityRequest.setTerrainType(city.getTerrainType());

            Map<String, String> enemies = new HashMap<>();

            //log.info("enemies "+city.getEnemy().toString());
            for (var enemy : city.getEnemy()) {
                enemies.put(enemy.getId().toString(), enemy.getName());
            }
            cityRequest.setEnemies(enemies);

            cityRequests.add(cityRequest);
        }
        return cityRequests;
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
                //characterRequest.setXCoord(character.getCity().getXCoord());
                //characterRequest.setYCoord(character.getCity().getYCoord());
                listCharacterRequest.add(characterRequest);
            }
        }
        return listCharacterRequest;
    }


    @PostMapping("/selectTarget")
    private ResponseEntity<?> selectTarget(@RequestBody Integer id) {
        User user = userService.getCurrentUser();
        GameCharacter gameCharacter = characterRepository.getById(user.getSelectedCharacterId());

        Enemy enemy = enemyRepository.findEnemyById(Long.valueOf(id));
        int currentX = gameCharacter.getCity().getXCoord();
        int currentY = gameCharacter.getCity().getYCoord();
        int targetX = enemy.getCity().getXCoord();
        int targetY = enemy.getCity().getYCoord();
        if (Math.abs(currentX - targetX) <= 1 && Math.abs(currentY - targetY) <= 1) {
            gameCharacter.setEnemyId(id);
            characterRepository.save(gameCharacter);
            return ResponseEntity.ok("ok");
        } else {
            return ResponseEntity.ok("Enemy is not within range");
        }
    }

    @GetMapping("/fight")
    private ResponseEntity<?> fightData() {
        User user = userService.getCurrentUser();
        GameCharacter gameCharacter = characterRepository.getById(user.getSelectedCharacterId());
        FightRequest fightRequest = new FightRequest();
        fightRequest.setCharacterName(gameCharacter.getCharacterName());
        fightRequest.setCharacterHp(gameCharacter.getHp());
        fightRequest.setCharacterLatestDam(gameCharacter.getLatestDam());

        Integer enemyId = gameCharacter.getEnemyId();
        if (enemyId != null) {
            fightRequest.setEnemyId(enemyId);
            Enemy enemy = enemyRepository.findEnemyById((long) enemyId);
            fightRequest.setEnemyHp(enemy.getHp());
            fightRequest.setEnemyLatestDam(enemy.getLatestDam());
        } else {
            fightRequest.setEnemyId(0);
        }

        return ResponseEntity.ok(fightRequest);
    }

    @GetMapping("/lvlup")
    private ResponseEntity<?> getPoints() {
        User user = userService.getCurrentUser();
        GameCharacter gameCharacter = characterRepository.getById(user.getSelectedCharacterId());
        LvlUpRequest lvlUpRequest = new LvlUpRequest();
        lvlUpRequest.setUnallocatedMainPoints(gameCharacter.getUnallocatedMainPoints());
        lvlUpRequest.setUnallocatedStrPoints(gameCharacter.getUnallocatedStrPoints());
        lvlUpRequest.setUnallocatedAgiPoints(gameCharacter.getUnallocatedAgiPoints());
        lvlUpRequest.setUnallocatedIntePoints(gameCharacter.getUnallocatedIntePoints());
        return ResponseEntity.ok(lvlUpRequest);
    }

    @PostMapping("/lvlup")
    private ResponseEntity<?> setPoints(@RequestBody LvlUpRequest lvlUpRequest) {
        User user = userService.getCurrentUser();
        GameCharacter gameCharacter = characterRepository.getById(user.getSelectedCharacterId());

        int sumClientMainPoints = lvlUpRequest.getStr() + lvlUpRequest.getAgi() + lvlUpRequest.getInte()
                + lvlUpRequest.getUnallocatedMainPoints();//points received from the client
        int sumServerMainPoints = gameCharacter.getUnallocatedMainPoints() + gameCharacter.getStr() +
                gameCharacter.getAgi() + gameCharacter.getInte();
        if (sumServerMainPoints == sumClientMainPoints) {
            gameCharacter.setUnallocatedMainPoints(lvlUpRequest.getUnallocatedMainPoints());
            gameCharacter.setStr(lvlUpRequest.getStr());
            gameCharacter.setAgi(lvlUpRequest.getAgi());
            gameCharacter.setInte(lvlUpRequest.getInte());
        }



        return ResponseEntity.ok("ok");
    }

}
