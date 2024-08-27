package com.ta.pocketRPG.domain.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class FightRequest {
    private CharacterRequest characterRequest;
    private List<EnemyRequest> enemyRequest;
}
