package com.ta.pocketRPG.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FightRequest {
    private String characterName;
    private int characterHp;
    private int characterLatestDam;
    private int enemyId;
    private int enemyHp;
    private int enemyLatestDam;
}
