package com.ta.pocketRPG.repository;

import com.ta.pocketRPG.domain.model.ListOfCities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListOfCitiesRepository extends JpaRepository<ListOfCities,Long> {
    //ListOfCities save(ListOfCities listOfCities);
    ListOfCities save(ListOfCities listOfCities);
}
