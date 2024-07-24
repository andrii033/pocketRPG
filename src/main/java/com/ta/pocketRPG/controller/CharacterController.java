package com.ta.pocketRPG.controller;

import com.ta.pocketRPG.component.EventGenerator;
import com.ta.pocketRPG.domain.dto.*;
import com.ta.pocketRPG.domain.model.*;
import com.ta.pocketRPG.repository.CharacterRepository;
import com.ta.pocketRPG.repository.CityRepository;
import com.ta.pocketRPG.repository.EnemyRepository;
import com.ta.pocketRPG.service.CharacterService;
import com.ta.pocketRPG.service.EnemyService;
import com.ta.pocketRPG.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/character")
public class CharacterController {

    private final CharacterService characterService;
    private final CharacterRepository characterRepository;
    private final UserService userService;
    private final CityRepository cityRepository;
    private final EnemyRepository enemyRepository;
    private final EnemyService enemyService;
    private final EventGenerator eventGenerator;

    public CharacterController(CharacterService characterService, CharacterRepository characterRepository, UserService userService, CityRepository cityRepository, EnemyRepository enemyRepository, EnemyService enemyService, EventGenerator eventGenerator) {
        this.characterService = characterService;
        this.characterRepository = characterRepository;
        this.userService = userService;
        this.cityRepository = cityRepository;
        this.enemyRepository = enemyRepository;
        this.enemyService = enemyService;
        this.eventGenerator = eventGenerator;
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

        return ResponseEntity.ok(user.getSelectedCharacterId());
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
                listCharacterRequest.add(characterRequest);
            }
        }
        return listCharacterRequest;
    }



    @GetMapping("/fight")
    private ResponseEntity<?> fightData(@RequestBody String id) {
        User user = userService.getCurrentUser();
        GameCharacter gameCharacter = characterRepository.getById(user.getSelectedCharacterId());

        if (gameCharacter.isWait()) {
            return ResponseEntity.ok("You are waiting.");

        }
//        FightRequest fightRequest = new FightRequest();
//        fightRequest.setCharacterName(gameCharacter.getCharacterName());
//        fightRequest.setCharacterHp(gameCharacter.getHp());
//        fightRequest.setCharacterLatestDam(gameCharacter.getLatestDam());
//
//        Integer enemyId = gameCharacter.getEnemyId();
//        if (enemyId != null) {
//            fightRequest.setEnemyId(enemyId);
//            Enemy enemy = enemyRepository.findEnemyById((long) enemyId);
//            fightRequest.setEnemyHp(enemy.getHp());
//            fightRequest.setEnemyLatestDam(enemy.getLatestDam());
//        } else {
//            fightRequest.setEnemyId(0);
//        }

        return ResponseEntity.ok("wait");
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

    @PostMapping("/move")
    private ResponseEntity<?> moveBattleCity(@RequestBody Long id) {
        User user = userService.getCurrentUser();
        GameCharacter gameCharacter = characterRepository.getById(user.getSelectedCharacterId());

        City battleCity1 = new City();
        battleCity1.setName("Battle City ");
        cityRepository.save(battleCity1);

        gameCharacter.setCity(cityRepository.getById(id));
        characterRepository.save(gameCharacter);

        List<Enemy> enemies = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Enemy enemy = new Enemy(); // Will be loaded from the database in the future
            enemy = enemyService.createEnemy(battleCity1);
            enemies.add(enemy);
        }

        battleCity1.setEnemy(enemies);
        cityRepository.save(battleCity1);

        List<EnemyRequest> enemiesRequest = new ArrayList<>();
        for (var x : enemies) {
            EnemyRequest enemy = new EnemyRequest();
            enemy.setArmorPiercing(x.getArmorPiercing());
            enemy.setDef(x.getDef());
            enemy.setId(x.getId());
            enemy.setName(x.getName());
            enemy.setStr(x.getStr());
            enemy.setAgi(x.getAgi());
            enemy.setInte(x.getInte());
            enemy.setHp(x.getHp());
            enemy.setLatestDam(x.getLatestDam());
            enemy.setCharId(x.getCharId());
            enemiesRequest.add(enemy);
        }

        eventGenerator.startFightCycle(battleCity1); // Start the fight cycle for the new city

        return ResponseEntity.ok(enemiesRequest);
    }


}
