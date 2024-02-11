package com.ta.pocketRPG.services;

import com.ta.pocketRPG.model.Enemy;
import com.ta.pocketRPG.repository.EnemyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnemyService {
    private final EnemyRepository enemyRepository;

    @Autowired
    public EnemyService(EnemyRepository enemyRepository) {
        this.enemyRepository = enemyRepository;
    }

    public Enemy saveEnemy(Enemy enemy) {
        return enemyRepository.save(enemy);
    }
}
