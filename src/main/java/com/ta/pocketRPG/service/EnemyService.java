package com.ta.pocketRPG.service;

import com.ta.pocketRPG.domain.dto.EnemyRequest;
import com.ta.pocketRPG.domain.model.City;
import com.ta.pocketRPG.domain.model.Enemy;
import com.ta.pocketRPG.repository.EnemyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        enemy.setHp(10);
        enemy.setInitiative(7);
        return enemy;
    }

    public Enemy findEnemyById(Long id){
        return enemyRepository.findEnemyById(id);
    }

    public List<EnemyRequest> findEnemiesAndMapToEnemyRequest(City city){
        List<EnemyRequest> enemyRequests = new ArrayList<>();
        List<Enemy> enemies = enemyRepository.findByCity(city);

        for (var x : enemies) {
                EnemyRequest enemy = new EnemyRequest();
                enemy.setArmorPiercing(x.getArmorPiercing());
                enemy.setDef(x.getDef());
                enemy.setId(x.getId());
                enemy.setName(x.getName());
                enemy.setStr(x.getStr());
                enemy.setAgi(x.getAgi());
                enemy.setInte(x.getInte());
                enemy.setHp(x.getHp());
                enemy.setLatestDam(x.getLatestDam());
                enemy.setCharId(x.getCharId());
                enemy.setInitiative(x.getInitiative());

                enemyRequests.add(enemy);
        }

        return enemyRequests;
    }

}
