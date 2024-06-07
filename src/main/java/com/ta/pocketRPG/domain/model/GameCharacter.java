package com.ta.pocketRPG.domain.model;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

@Data
@Entity
public class GameCharacter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    String characterName;
    private int str;
    private int agi;
    private int inte;

    private int def;
    private int physicalHarm;
    private int armorPiercing;
    private int reduceBlockDam;
    private int maxHealth;
    private int critChance;
    private int attackSpeed;
    private int tempAttackSpeed;
    private int avoidance;
    private int blockChance;
    private int magicDam;
    private int magicCritChance;
    private int manaRegen;
    private int maxMana;

    private int gold;
    private int res;
    private int hp;
    private int mana;

    private int mainPoints;
    private int secondaryPoints;
    private Integer enemyId;
    private int exp;
    private int lvl;
    private int latestDam;
    private int damageDone;



    @ManyToOne
    private User user;

    @ManyToOne
    private City city;

    public void addExp(int newExp){
        exp+=newExp;
    }

}