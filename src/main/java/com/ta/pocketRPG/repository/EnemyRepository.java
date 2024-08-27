package com.ta.pocketRPG.repository;

import com.ta.pocketRPG.domain.model.City;
import com.ta.pocketRPG.domain.model.Enemy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnemyRepository extends JpaRepository<Enemy,Long> {
    Enemy save(Enemy enemy);
    Enemy findEnemyById(Long id);
    Optional<Enemy> findById(Long id);
    List<Enemy> findByCity(City city);

    void deleteById(int enemyId);
}
