package com.ta.pocketRPG.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnemyRequest {
    private Long id;
    private String name;
    private int str=1;
    private int agi=1;
    private int inte=1;
    private int hp=10;
    private int latestDam;
    private int def;
    private int armorPiercing;
    private Long charId;
}
