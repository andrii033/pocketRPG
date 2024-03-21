package com.ta.pocketRPG.service;

import com.ta.pocketRPG.domain.model.City;
import com.ta.pocketRPG.domain.model.Enemy;
import org.springframework.stereotype.Service;

@Service
public class EnemyService {
    public Enemy createEnemy(City city){
        Enemy enemy = new Enemy();
        enemy.setAgi(1);
        enemy.setStr(1);
        enemy.setName("Enemy");
        enemy.setInte(1);
        enemy.setCity(city);
        return enemy;
    }
}
