package com.ta.pocketRPG.repository;

import com.ta.pocketRPG.domain.model.City;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City,Long> {
    City save(City city);
}
