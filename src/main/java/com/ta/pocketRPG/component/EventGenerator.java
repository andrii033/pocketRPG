package com.ta.pocketRPG.component;

import com.ta.pocketRPG.domain.model.Enemy;
import com.ta.pocketRPG.domain.model.GameCharacter;
import com.ta.pocketRPG.repository.CharacterRepository;
import com.ta.pocketRPG.repository.CityRepository;
import com.ta.pocketRPG.repository.EnemyRepository;
import com.ta.pocketRPG.service.CharacterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Component
public class EventGenerator {

    private final int rate = 5000;

    private final CharacterService characterService;
    private final EnemyRepository enemyRepository;
    private final Random random = new Random();
    private final CharacterRepository characterRepository;
    private final CityRepository cityRepository;

    List<GameCharacter> characterFightList;
    List<Enemy> enemies;

    private final Map<Long, Boolean> activeRooms = new HashMap<>();

    public EventGenerator(CharacterService characterService, EnemyRepository enemyRepository, CharacterRepository characterRepository, CityRepository cityRepository) {
        this.characterService = characterService;
        this.enemyRepository = enemyRepository;
        this.characterRepository = characterRepository;
        this.cityRepository = cityRepository;
    }

    @Scheduled(fixedRate = rate)
    @Transactional
    public void generateEvent() {
        for (Long cityId : activeRooms.keySet()) {
            log.info("City: " + cityId);
            if (activeRooms.get(cityId)) {
                characterFightList = characterRepository.findByCityId(cityId);
                enemies = enemyRepository.findByCityId(cityId);
                int sum = 0;
                for (Enemy enemy : enemies) {
                    sum += enemy.getHp();
                }
                if (sum > 0 && !characterFightList.isEmpty()) {
                    processRoomEvents(cityId);
                } else {
                    stopFightCycle(cityId);
                    //move to start city
                    for (GameCharacter character : characterFightList) {
                        character.setCity(cityRepository.findCityById(1));
                        //restore hp
                        character.setHp(20 + (character.getLvl() * 3) + character.getMaxHealth());
                    }
                    cityRepository.deleteById(cityId);
                }
            }
        }
    }

    @Transactional
    public void startFightCycle(Long cityId) {
        log.info("Starting Fight Cycle " + cityId);
        activeRooms.put(cityId, true);
    }

    @Transactional
    public void stopFightCycle(Long cityId) {
        //activeRooms.put(city.getId(), false);
        log.info("Stopping Fight Cycle " + cityId);
        activeRooms.remove(cityId);
    }

    private void processRoomEvents(Long cityId) {
        characterFightList = characterRepository.findByCityId(cityId);
        enemies = enemyRepository.findByCityId(cityId);


        //randomly searches for a target to attack
        for (Enemy enemy : enemies) {
            if (enemy.getCharId() == null) {
                Random random = new Random();
                int upperBound = characterFightList.size();
                int randomNumber = random.nextInt(upperBound);
                enemy.setCharId(characterFightList.get(randomNumber).getId());
            }
        }

        for (GameCharacter gameCharacter : characterFightList) {
            for (Enemy enemy : enemies) {

                //character attack
                if (gameCharacter.getEnemyId() == enemy.getId()) {
                    int damage = calculateDamage(gameCharacter, enemy);
                    enemy.setHp(enemy.getHp() - damage);
                    gameCharacter.setExp(gameCharacter.getExp() + damage);
                    lvlUp(gameCharacter);
                    if (enemy.getHp() <= 0) {
                        enemy.setHp(0);
                    }
                }
                //enemy attack
                if (Objects.equals(enemy.getCharId(), gameCharacter.getId()) && enemy.getHp() > 0) {
                    gameCharacter.setHp(gameCharacter.getHp() - (enemy.getStr() + enemy.getAgi()));
                    if (gameCharacter.getHp() <= 0) {
                        gameCharacter.setHp(0);
                        gameCharacter.setCity(cityRepository.findCityById(1));

                    }
                }
            }
        }

        enemyRepository.saveAll(enemies);
        characterService.saveAll(characterFightList);
    }

    private static void lvlUp(GameCharacter character) {
        if (character.getExp() > character.getLvl() * 50) {
            character.setExp(0);
            character.setLvl(character.getLvl() + 1);
            if (character.getLvl() % 2 == 0) {
                character.setUnallocatedMainPoints(character.getUnallocatedMainPoints() + 3);
            } else {
                character.addSecondaryPoints();
            }

            log.info("Character leveled up!");
        }
    }

    private int calculateDamage(GameCharacter character, Enemy enemy) {
        int damage;
        int minDam = 1 + (int) Math.ceil((double) character.getPhysicalHarm() / 2);
        int maxDam = 3 + (int) Math.ceil((double) character.getPhysicalHarm() / 2);

        damage = minDam + random.nextInt((maxDam - minDam) + 1); // Damage calculation
        int defTemp = enemy.getDef() - character.getArmorPiercing();
        if (defTemp > 0) {
            damage = damage - defTemp;
        }
        int luckChance = random.nextInt(1000) + 1; // 0.1 point crit chance for 1 point agility
        if (luckChance < character.getCritChance()) {
            damage *= 2;
        }
        return damage;
    }
}
