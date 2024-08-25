package com.ta.pocketRPG.domain.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class FightRequest {
    private CharacterRequest characterRequest;
    private EnemyRequest enemyRequest;
}
