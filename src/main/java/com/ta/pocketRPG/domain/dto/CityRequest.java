package com.ta.pocketRPG.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CityRequest {
    private int xCoord;
    private int yCoord;
    private String terrainType;
}
