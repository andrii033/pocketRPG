package com.ta.pocketRPG.service;

import com.ta.pocketRPG.domain.model.City;
import com.ta.pocketRPG.domain.model.Enemy;
import com.ta.pocketRPG.repository.EnemyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnemyService {
    @Autowired
    EnemyRepository enemyRepository;
    public Enemy createEnemy(City city){
        Enemy enemy = new Enemy();
        enemy.setAgi(1);
        enemy.setStr(1);
        enemy.setName("Enemy");
        enemy.setInte(1);
        enemy.setCity(city);
        return enemy;
    }

    public Enemy findEnemyById(Long id){
        return enemyRepository.findEnemyById(id);
    }
}
