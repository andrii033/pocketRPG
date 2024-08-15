package com.ta.pocketRPG.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCharacterRequest {
    String name;
    long id;
    int str;
    int agi;
    int inte;
    int lvl;
}
