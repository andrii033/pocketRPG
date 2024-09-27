package com.ta.pocketRPG.controller;

import com.ta.pocketRPG.component.EventGenerator;
import com.ta.pocketRPG.domain.dto.*;
import com.ta.pocketRPG.domain.model.City;
import com.ta.pocketRPG.domain.model.Enemy;
import com.ta.pocketRPG.domain.model.GameCharacter;
import com.ta.pocketRPG.domain.model.User;
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
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/character")
public class CharacterController {

    private final CharacterService characterService;
    private final CharacterRepository characterRepository;
    private final UserService userService;
    private final CityRepository cityRepository;
    private final EnemyService enemyService;
    private final EventGenerator eventGenerator;

    private static int count;
    private final EnemyRepository enemyRepository;

    public CharacterController(CharacterService characterService, CharacterRepository characterRepository,
                               UserService userService, CityRepository cityRepository, EnemyService enemyService,
                               EventGenerator eventGenerator, EnemyRepository enemyRepository) {
        this.characterService = characterService;
        this.characterRepository = characterRepository;
        this.userService = userService;
        this.cityRepository = cityRepository;
        this.enemyService = enemyService;
        this.eventGenerator = eventGenerator;
        this.enemyRepository = enemyRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCharacter(@RequestBody CreateCharacterRequest characterRequest) {
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
        List<CreateCharacterRequest> listCharacterRequest = createCharacterRequestList();
        if (!listCharacterRequest.isEmpty()) {
            return ResponseEntity.ok(listCharacterRequest);
        } else {
            return ResponseEntity.ok(listCharacterRequest); // Character request not found
        }
    }

    private List<CreateCharacterRequest> createCharacterRequestList() {
        List<CreateCharacterRequest> listCharacterRequest = new ArrayList<>();
        List<GameCharacter> gameCharacter = characterRepository.findGameCharacterByUser(userService.getCurrentUser());

        if (gameCharacter != null) {
            for (GameCharacter character : gameCharacter) {
                CreateCharacterRequest characterRequest = new CreateCharacterRequest();
                characterRequest.setName(character.getCharacterName());
                characterRequest.setId(character.getId());
                characterRequest.setStr(character.getStr());
                characterRequest.setInte(character.getInte());
                characterRequest.setAgi(character.getAgi());
                characterRequest.setLvl(character.getLvl());
                listCharacterRequest.add(characterRequest);
            }
        }
        return listCharacterRequest;
    }

    @PostMapping("/choose")
    public ResponseEntity<?> chooseCharacter(@RequestBody String id) {
        User user = userService.getCurrentUser();
        user.setSelectedCharacterId(Long.valueOf(id));
        userService.save(user);

        GameCharacter gameCharacter = characterRepository.findById(Long.parseLong(id));
        CharacterRequest characterRequest = new CharacterRequest();
        characterRequest.setCharacterName(gameCharacter.getCharacterName());
        characterRequest.setId(gameCharacter.getId());
        characterRequest.setStr(gameCharacter.getStr());
        characterRequest.setInte(gameCharacter.getInte());
        characterRequest.setAgi(gameCharacter.getAgi());

        characterRequest.setDef(gameCharacter.getDef());
        characterRequest.setHp(gameCharacter.getHp());
        characterRequest.setMana(gameCharacter.getMana());
        characterRequest.setInitiative(gameCharacter.getInitiative());

        characterRequest.setLvl(gameCharacter.getLvl());

        return ResponseEntity.ok(characterRequest);
    }

    @GetMapping("/getCharacter")
    public ResponseEntity<?> characterInfo() {
        User user = userService.getCurrentUser();

        Optional<GameCharacter> gameCharacter = characterRepository.findById(user.getSelectedCharacterId());

//        gameCharacter.get().setHp(20 + (gameCharacter.get().getLvl() * 3) + gameCharacter.get().getMaxHealth());
//        gameCharacter.get().setCity(cityRepository.findCityById(1));
//        characterRepository.save(gameCharacter.get());

        CharacterRequest characterRequest = new CharacterRequest();
        characterRequest.setCharacterName(gameCharacter.get().getCharacterName());
        characterRequest.setId(gameCharacter.get().getId());
        characterRequest.setStr(gameCharacter.get().getStr());
        characterRequest.setInte(gameCharacter.get().getInte());
        characterRequest.setAgi(gameCharacter.get().getAgi());

        characterRequest.setDef(gameCharacter.get().getDef());
        characterRequest.setHp(gameCharacter.get().getHp());
        characterRequest.setMana(gameCharacter.get().getMana());
        characterRequest.setInitiative(gameCharacter.get().getInitiative());

        characterRequest.setLvl(gameCharacter.get().getLvl());

        return ResponseEntity.ok(characterRequest);
    }


    @PostMapping("/fight")
    public ResponseEntity<?> fightData(@RequestBody String id) {
        User user = userService.getCurrentUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated.");
        }
        FightRequest fightRequest = new FightRequest();
        GameCharacter gameCharacter;
        try {
            gameCharacter = characterRepository.getById(user.getSelectedCharacterId());
            fightRequest.setCharacterRequest(mapGameCharacterToCharacterRequest(gameCharacter));
            List<EnemyRequest> enemyRequests = enemyService.findEnemiesAndMapToEnemyRequest(gameCharacter.getCity());
            fightRequest.setEnemyRequest(enemyRequests);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve character.");
        }

        try {
            gameCharacter.setEnemyId(Integer.parseInt(id));
            characterRepository.save(gameCharacter);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid enemy ID format.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save character.");
        }

        return ResponseEntity.ok(fightRequest);
    }



    private CharacterRequest mapGameCharacterToCharacterRequest(GameCharacter gameCharacter) {
        CharacterRequest characterRequest = new CharacterRequest();

        // Map basic fields
        characterRequest.setCharacterName(gameCharacter.getCharacterName());
        characterRequest.setId(gameCharacter.getId());

        characterRequest.setStr(gameCharacter.getStr());
        characterRequest.setAgi(gameCharacter.getAgi());
        characterRequest.setInte(gameCharacter.getInte());

        characterRequest.setDef(gameCharacter.getDef());
        characterRequest.setHp(gameCharacter.getHp());
        characterRequest.setMana(gameCharacter.getMana());

        characterRequest.setPhysicalHarm(gameCharacter.getPhysicalHarm());
        characterRequest.setArmorPiercing(gameCharacter.getArmorPiercing());
        characterRequest.setReduceBlockDam(gameCharacter.getReduceBlockDam());
        characterRequest.setMaxHealth(gameCharacter.getMaxHealth());

        characterRequest.setCritChance(gameCharacter.getCritChance());
        characterRequest.setAttackSpeed(gameCharacter.getAttackSpeed());
        characterRequest.setAvoidance(gameCharacter.getAvoidance());
        characterRequest.setBlockChance(gameCharacter.getBlockChance());

        characterRequest.setMagicDam(gameCharacter.getMagicDam());
        characterRequest.setMagicCritChance(gameCharacter.getMagicCritChance());
        characterRequest.setManaRegen(gameCharacter.getManaRegen());
        characterRequest.setMaxMana(gameCharacter.getMaxMana());

        characterRequest.setGold(gameCharacter.getGold());
        characterRequest.setRes(gameCharacter.getRes());

        characterRequest.setExp(gameCharacter.getExp());
        characterRequest.setLvl(gameCharacter.getLvl());

        characterRequest.setUnallocatedMainPoints(gameCharacter.getUnallocatedMainPoints());
        characterRequest.setUnallocatedStrPoints(gameCharacter.getUnallocatedStrPoints());
        characterRequest.setUnallocatedAgiPoints(gameCharacter.getUnallocatedAgiPoints());
        characterRequest.setUnallocatedIntePoints(gameCharacter.getUnallocatedIntePoints());

        characterRequest.setLatestDamage(gameCharacter.getLatestDam());

        return characterRequest;
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

        lvlUpRequest.setAgi(gameCharacter.getAgi());
        lvlUpRequest.setStr(gameCharacter.getStr());
        lvlUpRequest.setInte(gameCharacter.getInte());

        lvlUpRequest.setPhysicalHarm(gameCharacter.getPhysicalHarm());
        lvlUpRequest.setArmorPiercing(gameCharacter.getArmorPiercing());
        lvlUpRequest.setReduceBlockDam(gameCharacter.getReduceBlockDam());
        lvlUpRequest.setMaxHealth(gameCharacter.getMaxHealth());

        lvlUpRequest.setCritChance(gameCharacter.getCritChance());
        lvlUpRequest.setAttackSpeed(gameCharacter.getAttackSpeed());
        lvlUpRequest.setAvoidance(gameCharacter.getAvoidance());
        lvlUpRequest.setBlockChance(gameCharacter.getBlockChance());

        lvlUpRequest.setMagicDam(gameCharacter.getMagicDam());
        lvlUpRequest.setMagicCritChance(gameCharacter.getMagicCritChance());
        lvlUpRequest.setManaRegen(gameCharacter.getManaRegen());
        lvlUpRequest.setMaxMana(gameCharacter.getMaxMana());

        return ResponseEntity.ok(lvlUpRequest);
    }

//    @PostMapping("/lvlup")
//    private ResponseEntity<?> setPoints(@RequestBody LvlUpRequest lvlUpRequest) {
//        User user = userService.getCurrentUser();
//        GameCharacter gameCharacter = characterRepository.getById(user.getSelectedCharacterId());
//
//        int sumClientMainPoints = lvlUpRequest.getStr() + lvlUpRequest.getAgi() + lvlUpRequest.getInte()
//                + lvlUpRequest.getUnallocatedMainPoints();//points received from the client
//        int sumServerMainPoints = gameCharacter.getUnallocatedMainPoints() + gameCharacter.getStr() +
//                gameCharacter.getAgi() + gameCharacter.getInte();
//        if (sumServerMainPoints == sumClientMainPoints) {
//            gameCharacter.setUnallocatedMainPoints(lvlUpRequest.getUnallocatedMainPoints());
//            gameCharacter.setStr(lvlUpRequest.getStr());
//            gameCharacter.setAgi(lvlUpRequest.getAgi());
//            gameCharacter.setInte(lvlUpRequest.getInte());
//        }
//
//        return ResponseEntity.ok("ok");
//    }

    @PostMapping("/move")
    private ResponseEntity<?> moveBattleCity(@RequestBody Long id) {
        User user = userService.getCurrentUser();
        GameCharacter gameCharacter = characterRepository.getById(user.getSelectedCharacterId());

        gameCharacter.setHp(20 + (gameCharacter.getLvl() * 3) + gameCharacter.getMaxHealth());

        log.info("moveBattleCity" + user);

        City battleCity1 = new City();
        battleCity1.setName("Battle City " + count);
        count++;
        cityRepository.save(battleCity1);

        gameCharacter.setCity(battleCity1);
        characterRepository.save(gameCharacter);

        List<Enemy> enemies = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Enemy enemy = enemyService.createEnemy(battleCity1);
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

        eventGenerator.startFightCycle(battleCity1.getId()); // Start the fight cycle for the new city

        return ResponseEntity.ok(enemiesRequest);
    }


}
