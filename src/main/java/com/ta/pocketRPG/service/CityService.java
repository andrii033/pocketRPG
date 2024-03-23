package com.ta.pocketRPG.service;


import com.ta.pocketRPG.domain.model.City;
import com.ta.pocketRPG.domain.model.Enemy;
import com.ta.pocketRPG.domain.model.ListOfCities;
import com.ta.pocketRPG.repository.CityRepository;
import com.ta.pocketRPG.repository.ListOfCitiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private ListOfCitiesRepository listOfCitiesRepository;
    @Autowired
    private EnemyService enemyService;

    @PostConstruct
    public void initializeCity() {
        ListOfCities listOfCities = new ListOfCities();
        listOfCities.setName("Start city");

        // Save the ListOfCities first
        listOfCities = listOfCitiesRepository.save(listOfCities);

        List<City> cities = new ArrayList<>();
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                City city = new City();
                city.setXCoord(x);
                city.setYCoord(y);
                city.setTerrainType("Grass");

                Enemy enemy = enemyService.createEnemy(city);
                city.getEnemy().add(enemy);

                // Set the ListOfCities for each City
                city.setListOfCities(listOfCities);

                cities.add(city);
            }
        }

        // Save all the City entities
        cityRepository.saveAll(cities);
    }
    public City getById(Long id){
        return cityRepository.getById(id);
    }
}
