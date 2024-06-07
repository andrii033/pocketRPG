package com.ta.pocketRPG.component;

import com.ta.pocketRPG.domain.model.Enemy;
import com.ta.pocketRPG.domain.model.GameCharacter;
import com.ta.pocketRPG.repository.EnemyRepository;
import com.ta.pocketRPG.service.CharacterService;
import com.ta.pocketRPG.service.CityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Slf4j
@Component
public class EventGenerator {

    private final CharacterService characterService;
    private final CityService cityService;
    private final EnemyRepository enemyRepository;
    private final Random random = new Random();

    public EventGenerator(CharacterService characterService, CityService cityService, EnemyRepository enemyRepository) {
        this.characterService = characterService;
        this.cityService = cityService;
        this.enemyRepository = enemyRepository;
    }

    @Scheduled(fixedRate = 1000)
    @Transactional
    public void generateEvent() {
        List<GameCharacter> characterFightList = characterService.charactersWithEnemies();
        for (var character : characterFightList) {
            Enemy enemy = enemyRepository.findEnemyById((long) character.getEnemyId()); //find enemy

            int damag = 0;
            int tempHp = enemy.getHp();
            int minDam = 1 + (int) Math.ceil(character.getPhysicalHarm() / 2);
            int maxDam = 3 + (int) Math.ceil(character.getPhysicalHarm() / 2);

            int atackSpeed = 30 + character.getAttackSpeed() + (character.getLvl() / 2);//calculate attack speed
            int tempSpeed = character.getTempAttackSpeed();
            tempSpeed = tempSpeed + atackSpeed;
            if (tempSpeed - 60 > 0) {
                character.setTempAttackSpeed(tempSpeed-60);
            }else{
                character.setTempAttackSpeed(tempSpeed);
                continue;
            }
            damag = minDam + random.nextInt((maxDam - minDam) + 1); //damage calculation
            int defTemp = enemy.getDef() - character.getArmorPiercing();
            if (defTemp > 0) {
                damag = damag - defTemp;
            }
            int luckChance = random.nextInt(1000) + 1; //0.1 point crit chance for 1 point agility
            if (luckChance < character.getCritChance()) {
                damag *= 2;
            }

            character.setLatestDam(damag); //to send to the client

            enemy.setHp(enemy.getHp() - damag); //attack
            character.addExp((int)Math.ceil(damag/3));
            System.out.println("exp "+(int)Math.ceil(damag/3));

            if(character.getExp() < character.getLvl()*50){
                character.setExp(0);
                character.setLvl(character.getLvl()+1);
                log.info("character lvl up !!!!!!!!");
            }

            log.info("enemyHp " + enemy.getHp() + " char exp " + character.getExp());

            if (enemy.getHp() <= 0) {
                character.setEnemyId(null);
                log.info("you have defeated the enemy");
            }
            enemy.setCharId(character.getId());
        }
        characterService.saveAll(characterFightList);
    }
}
