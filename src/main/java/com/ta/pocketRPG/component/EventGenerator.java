package com.ta.pocketRPG.component;

import com.ta.pocketRPG.domain.model.Enemy;
import com.ta.pocketRPG.domain.model.GameCharacter;
import com.ta.pocketRPG.repository.EnemyRepository;
import com.ta.pocketRPG.service.CharacterService;
import com.ta.pocketRPG.service.CityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Component
public class EventGenerator {

    @Autowired
    CharacterService characterService;
    @Autowired
    CityService cityService;
    @Autowired
    EnemyRepository enemyRepository;

    List<GameCharacter> characterFightList;
    Enemy enemy;

    @Scheduled(fixedRate = 1000)
    @Transactional
    public void generateEvent() {
        characterFightList = characterService.charactersWithEnemies();
        for (var character : characterFightList) {
            enemy = enemyRepository.findEnemyById((long) character.getEnemyId()); //find enemy
            int temp = enemy.getHp();
            enemy.setHp(enemy.getHp() - character.getStr()); //attack
            character.addExp(temp - enemy.getHp());
            log.info("enemyHp " + enemy.getHp() + " char exp " + character.getExp());
            if (enemy.getHp() <= 0) {
                character.setEnemyId(0);
                log.info("you have defeated the enemy");
            }
        }
        characterService.saveAll(characterFightList);
    }
}
