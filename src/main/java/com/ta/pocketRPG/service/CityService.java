package com.ta.pocketRPG.service;


import com.ta.pocketRPG.domain.model.City;
import com.ta.pocketRPG.domain.model.Enemy;
import com.ta.pocketRPG.repository.CityRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class CityService {

    private final CityRepository cityRepository;

    private final EnemyService enemyService;

    public CityService(CityRepository cityRepository, EnemyService enemyService) {
        this.cityRepository = cityRepository;
        this.enemyService = enemyService;
    }

    @PostConstruct
    public void initializeCity() {
        List<City> cities = new ArrayList<>();

        City startingCity = new City();
        startingCity.setName("Starting city");
        cities.add(startingCity);

//        City battleField = new City();
//        battleField.setName("Battlefield");
//        cities.add(battleField);

//        List<Enemy> enemies = new ArrayList<>();
//        for (int i=0; i<5; i++) {
//            Enemy enemy = enemyService.createEnemy(battleField);
//            enemies.add(enemy);
//        }
//        battleField.setEnemy(enemies);

        // Save the ListOfCities first
        cityRepository.saveAll(cities);

    }


    public City getById(Long id){
        return cityRepository.getById(id);
    }
}
