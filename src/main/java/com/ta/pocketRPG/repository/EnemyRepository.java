package com.ta.pocketRPG.repository;

import com.ta.pocketRPG.model.Enemy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnemyRepository extends JpaRepository<Enemy,Long> {
}
