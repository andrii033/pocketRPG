package com.ta.pocketRPG;

import com.ta.pocketRPG.data.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}