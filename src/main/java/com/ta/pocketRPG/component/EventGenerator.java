package com.ta.pocketRPG.component;

import com.ta.pocketRPG.domain.model.City;
import com.ta.pocketRPG.domain.model.Enemy;
import com.ta.pocketRPG.domain.model.GameCharacter;
import com.ta.pocketRPG.repository.EnemyRepository;
import com.ta.pocketRPG.service.CharacterService;
import com.ta.pocketRPG.service.EnemyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class EventGenerator {

    private final CharacterService characterService;
    private final EnemyRepository enemyRepository;
    private final Random random = new Random();
    private final EnemyService enemyService;
    private AtomicBoolean stopFlag = new AtomicBoolean(false);
    private int counter = 0;

    private final Map<Long, Boolean> activeRooms = new HashMap<>();

    public EventGenerator(CharacterService characterService, EnemyRepository enemyRepository, EnemyService enemyService) {
        this.characterService = characterService;
        this.enemyRepository = enemyRepository;
        this.enemyService = enemyService;
    }

    @Scheduled(fixedRate = 1000)
    @Transactional
    public void generateEvent() {
        if (counter == 10 || stopFlag.get()) {
            for (Long cityId : activeRooms.keySet()) {
                //log.info("City: " + cityId);
                if (activeRooms.get(cityId)) {
                    processRoomEvents(cityId);
                }
            }
            counter = 0;
            stopFlag.set(false);
        } else {
            counter++;
        }
    }

    @Transactional
    public void startFightCycle(City city) {
        activeRooms.put(city.getId(), true);
    }

    @Transactional
    public void stopFightCycle(City city) {
        //activeRooms.put(city.getId(), false);
        activeRooms.remove(city.getId());
    }

    private void processRoomEvents(Long cityId) {
        List<GameCharacter> characterFightList = characterService.getCharactersByCity(cityId);
        List<Enemy> enemies = enemyRepository.findByCityId(cityId);

        List<Combatant> queue = new ArrayList<>();

        // add all character
        for (GameCharacter character : characterFightList) {
            queue.add(new Combatant(character, character.getInitiative()));
        }

        // add all enemies
        for (Enemy enemy : enemies) {
            queue.add(new Combatant(enemy, enemy.getInitiative()));
        }

        // sort
        Collections.sort(queue, Comparator.comparingInt(Combatant::getInitiative).reversed());

        for (var combatant : queue) {
            //Enemy enemy = enemyRepository.findById(Long.valueOf(character.getEnemyId())).orElse(null); // Find enemy


//            int attackSpeed = 30 + character.getAttackSpeed() + (character.getLvl() / 2); // Calculate attack speed
//            int tempSpeed = character.getTempAttackSpeed();
//            tempSpeed = tempSpeed + attackSpeed;
//            if (tempSpeed - 60 > 0) {
//                character.setTempAttackSpeed(tempSpeed - 60);
//            } else {
//                character.setTempAttackSpeed(tempSpeed);
//                continue;
//            }
//
//            int damage = calculateDamage(character, enemy); // Calculate damage
//            character.setLatestDam(damage); // To send to the client
//            enemy.setHp(enemy.getHp() - damage); // Attack
//
//            int exp = (int) Math.ceil((double) damage / 3); // Calculate experience
//            character.addExp(exp);
//
//            log.info("Added exp " + exp + " damage " + damage + " char exp " + character.getExp());
//
//            lvlUp(character);
//
//            if (enemy.getHp() <= 0) {
//                log.info("You have defeated the enemy with id " + character.getEnemyId());
//                long enemyId = character.getEnemyId();
//                character.setEnemyId(0);
//                enemyRepository.deleteById(enemyId);
//            }
//            enemy.setCharId(character.getId());
        }
        characterService.saveAll(characterFightList);
    }

//    private static void lvlUp(GameCharacter character) {
//        if (character.getExp() > character.getLvl() * 50) {
//            character.setExp(0);
//            character.setLvl(character.getLvl() + 1);
//            if (character.getLvl() % 2 == 0) {
//                character.setUnallocatedMainPoints(character.getUnallocatedMainPoints() + 3);
//            } else {
//                character.addSecondaryPoints();
//            }
//
//            log.info("Character leveled up!");
//        }
//    }
//
//    private int calculateDamage(GameCharacter character, Enemy enemy) {
//        int damage;
//        int minDam = 1 + (int) Math.ceil((double) character.getPhysicalHarm() / 2);
//        int maxDam = 3 + (int) Math.ceil((double) character.getPhysicalHarm() / 2);
//
//        damage = minDam + random.nextInt((maxDam - minDam) + 1); // Damage calculation
//        int defTemp = enemy.getDef() - character.getArmorPiercing();
//        if (defTemp > 0) {
//            damage = damage - defTemp;
//        }
//        int luckChance = random.nextInt(1000) + 1; // 0.1 point crit chance for 1 point agility
//        if (luckChance < character.getCritChance()) {
//            damage *= 2;
//        }
//        return damage;
//    }
}

class Combatant {
    private final Object character;
    private final int initiative;

    public Combatant(Object character, int initiative) {
        this.character = character;
        this.initiative = initiative;
    }

    public Object getCharacter() {
        return character;
    }

    public int getInitiative() {
        return initiative;
    }
}
