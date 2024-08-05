package com.ta.pocketRPG.repository;

import com.ta.pocketRPG.domain.model.Party;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<Party, Integer> {
}
