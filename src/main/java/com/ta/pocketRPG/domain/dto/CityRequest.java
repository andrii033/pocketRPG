package com.ta.pocketRPG.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CityRequest {
    private Map<String,String> enemies;
}
