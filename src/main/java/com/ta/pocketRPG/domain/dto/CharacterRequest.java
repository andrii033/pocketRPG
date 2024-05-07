package com.ta.pocketRPG.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CharacterRequest {
    private String characterName;
    private Long id;
    private int str;
    private int agi;
    private int inte;
    private int gold;
    private int res;
    private int xCoord;
    private int yCoord;
}
