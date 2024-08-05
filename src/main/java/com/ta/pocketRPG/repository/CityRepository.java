package com.ta.pocketRPG.repository;

import com.ta.pocketRPG.domain.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City,Long> {
    City save(City city);
    //List<City> findByListOfCitiesId(Long listOfCitiesId);
    List<City> findById (long id);
}

