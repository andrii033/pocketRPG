package com.ta.pocketRPG.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FightRequest {
    private String characterName;
    private String characterHp;
}
