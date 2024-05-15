package com.ta.pocketRPG.domain.model;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

@Data
@Entity
public class GameCharacter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Getter
    String characterName;
    int str;
    int agi;
    int inte;

    int physicalHarm;
    int armorPiercing;
    int reduceBlockDam;
    int maxHealth;
    int critChance;
    int attackSpeed;
    int avoidance;
    int blockChance;
    int magicDam;
    int magicCritChance;
    int manaRegen;
    int maxMana;

    int gold;
    int res;
    int hp;
    int mana;

    int mainPoints;
    int secondaryPoints;
    Integer enemyId;
    long exp;
    int lvl;
    int latestDam;
    int damageDone;



    @ManyToOne
    private User user;

    @ManyToOne
    private City city;

    public void addExp(int newExp){
        exp+=newExp;
    }

}