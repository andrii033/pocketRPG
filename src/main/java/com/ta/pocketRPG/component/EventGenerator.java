package com.ta.pocketRPG.component;

import com.ta.pocketRPG.domain.model.Enemy;
import com.ta.pocketRPG.domain.model.GameCharacter;
import com.ta.pocketRPG.repository.EnemyRepository;
import com.ta.pocketRPG.service.CharacterService;
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
    private final EnemyRepository enemyRepository;
    private final Random random = new Random();

    public EventGenerator(CharacterService characterService, EnemyRepository enemyRepository) {
        this.characterService = characterService;
        this.enemyRepository = enemyRepository;
    }

    @Scheduled(fixedRate = 1000)
    @Transactional
    public void generateEvent() {
        List<GameCharacter> characterFightList = characterService.charactersWithEnemies();
        for (var character : characterFightList) {
            Enemy enemy = enemyRepository.findEnemyById((long) character.getEnemyId()); //find enemy

            int atackSpeed = 30 + character.getAttackSpeed() + (character.getLvl() / 2);//calculate attack speed
            int tempSpeed = character.getTempAttackSpeed();
            tempSpeed = tempSpeed + atackSpeed;
            if (tempSpeed - 60 > 0) {
                character.setTempAttackSpeed(tempSpeed-60);
            }else{
                character.setTempAttackSpeed(tempSpeed);
                continue;
            }

            int damage = calculateDamage(character, enemy); //damage
            character.setLatestDam(damage); //to send to the client
            enemy.setHp(enemy.getHp() - damage); //attack

            int exp = (int)Math.ceil((double) damage/3);//exp
            character.addExp(exp);

            System.out.println("add exp "+exp+" damage "+damage+" char exp "+character.getExp());

            lvlUp(character);

            if (enemy.getHp() <= 0) {
                log.info("you have defeated the enemy with id "+character.getEnemyId());
                long enemyId=character.getEnemyId();
                character.setEnemyId(null);
                enemyRepository.deleteById(enemyId);
            }
            enemy.setCharId(character.getId());
        }
        characterService.saveAll(characterFightList);
    }

    private static void lvlUp(GameCharacter character) {
        if(character.getExp() > character.getLvl()*50){
            character.setExp(0);
            character.setLvl(character.getLvl()+1);
            if(character.getLvl() % 2 == 0 ){
                character.setUnallocatedMainPoints(character.getUnallocatedMainPoints()+3);
            }else {
                character.addSecondaryPoints();
            }

            log.info("character lvl up !!!!!!!!");
        }
    }

    private int calculateDamage(GameCharacter character, Enemy enemy) {
        int damag;
        int minDam = 1 + (int) Math.ceil((double) character.getPhysicalHarm() / 2);
        int maxDam = 3 + (int) Math.ceil((double) character.getPhysicalHarm() / 2);

        damag = minDam + random.nextInt((maxDam - minDam) + 1); //damage calculation
        int defTemp = enemy.getDef() - character.getArmorPiercing();
        if (defTemp > 0) {
            damag = damag - defTemp;
        }
        int luckChance = random.nextInt(1000) + 1; //0.1 point crit chance for 1 point agility
        if (luckChance < character.getCritChance()) {
            damag *= 2;
        }
        return damag;
    }
}
