package com.ta.pocketRPG.repository;

import com.ta.pocketRPG.domain.model.Enemy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnemyRepository extends JpaRepository<Enemy,Long> {
    Enemy save(Enemy enemy);
}
