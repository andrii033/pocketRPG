package com.ta.pocketRPG.controller;

import com.ta.pocketRPG.component.RequestRateLimiter;
import com.ta.pocketRPG.domain.dto.CharacterRequest;
import com.ta.pocketRPG.domain.dto.FightRequest;
import com.ta.pocketRPG.domain.model.Enemy;
import com.ta.pocketRPG.domain.model.GameCharacter;
import com.ta.pocketRPG.repository.CharacterRepository;
import com.ta.pocketRPG.service.CharacterService;
import com.ta.pocketRPG.service.EnemyService;
import com.ta.pocketRPG.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/create")
    public String createCharacter(@RequestBody CharacterRequest characterRequest) {
        characterService.createCharacter(characterRequest);
        return "create";
    }

    //    @PostMapping("/fight")
//    public FightRequest fight(@RequestBody FightRequest fightRequest){
//        if (rateLimiter.allowRequest()) {
//            GameCharacter gameCharacter=characterRepository.findGameCharacterByUser(userService.getCurrentUser());
//            Enemy enemy= enemyService.findEnemyById((long) gameCharacter.getEnemyId());
//            fightRequest.setCharacterName(gameCharacter.getCharacterName());
//            fightRequest.setCharacterLatestDam(gameCharacter.getLatestDam());
//            fightRequest.setCharacterHp(gameCharacter.getHp());
//            fightRequest.setEnemyName(enemy.getName());
//            fightRequest.setEnemyHp(enemy.getHp());
//            fightRequest.setEnemyLatestDam(enemy.getLatestDam());
//            return fightRequest;
//        } else {
//            // Return an error response or handle the rate limit exceeded scenario
//            return fightRequest;
//        }
//    }
    @PostMapping("/fight")
    public ResponseEntity<?> fight(@RequestBody FightRequest fightRequest) {
        if (rateLimiter.allowRequest()) {
            GameCharacter gameCharacter = characterRepository.findGameCharacterByUser(userService.getCurrentUser());
            if (gameCharacter != null && gameCharacter.getEnemyId() != 0) {
                Enemy enemy = enemyService.findEnemyById((long) gameCharacter.getEnemyId());
                if (enemy != null) {
                    fightRequest.setCharacterName(gameCharacter.getCharacterName());
                    fightRequest.setCharacterLatestDam(gameCharacter.getLatestDam());
                    fightRequest.setCharacterHp(gameCharacter.getHp());
                    fightRequest.setEnemyName(enemy.getName());
                    fightRequest.setEnemyHp(enemy.getHp());
                    fightRequest.setEnemyLatestDam(enemy.getLatestDam());
                    return ResponseEntity.ok(fightRequest);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Enemy not found");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No enemy assigned to the character");
            }
        } else {
            // Handle the rate limit exceeded scenario
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate limit exceeded");
        }
    }

}
