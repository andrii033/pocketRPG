package com.ta.pocketRPG.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnemyRequest {
    private Long id;
    private String name;
    private int str;
    private int agi;
    private int inte;
    private int hp;
    private int latestDam;
    private int def;
    private int armorPiercing;
    private Long charId;
}
