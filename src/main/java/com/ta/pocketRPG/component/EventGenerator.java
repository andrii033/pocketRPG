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
import java.util.Random;


@Slf4j
@Component
public class EventGenerator {

    @Autowired
    CharacterService characterService;
    @Autowired
    CityService cityService;
    @Autowired
    EnemyRepository enemyRepository;

    Enemy enemy;

    @Scheduled(fixedRate = 1000)
    @Transactional
    public void generateEvent() {
        List<GameCharacter> characterFightList = characterService.charactersWithEnemies();
        for (var character : characterFightList) {
            enemy = enemyRepository.findEnemyById((long) character.getEnemyId()); //find enemy

            int damag = 0;
            int temp = enemy.getHp();
            Random random = new Random();
            int minDam = 1 + (int) Math.ceil(character.getPhysicalHarm() / 2);
            int maxDam = 3 + (int) Math.ceil(character.getPhysicalHarm() / 2);
            log.info("max " + maxDam + " min " + minDam);
            damag = minDam + random.nextInt((maxDam - minDam) + 1); //damage calculation
            int luckChance = random.nextInt(1000) + 1; //0.1 point crit chance for 1 point agility
            if (luckChance < character.getCritChance()) {
                damag *= 2;
            }

            character.setLatestDam(damag); //calculate the damage

            enemy.setHp(enemy.getHp() - character.getLatestDam()); //attack
            character.addExp(temp - enemy.getHp());
            log.info("enemyHp " + enemy.getHp() + " char exp " + character.getExp() + " dam " + character.getLatestDam());
            if (enemy.getHp() <= 0) {
                character.setEnemyId(null);
                log.info("you have defeated the enemy");
            }
            enemy.setCharId(character.getId());
        }
        characterService.saveAll(characterFightList);
    }
}
