package com.ta.pocketRPG.model;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

@Data
@Entity
public class GameCharacter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter
    private String characterName;
//    private int lvl=1;
//    private int resets;
//    private int exp;
//    private int hp_max;
//    private int hp;
//    private int mana_max;
//    private int mana;
//    private int physical_damage;
//    private int armor_piercing;
//    private int crit_hit_chance;
//    private int chance_of_blocking;
//    private int block_damage_reduction;
//    private int armor;
//    private int chance_of_goal;
//    private int attack_speed;
//    private int magical_damage;
//    private int mana_regen;
//    private int chance_of_magical_crit;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
