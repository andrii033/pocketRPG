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
import java.util.Random;

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
        List<ListOfCities> listOfCities = new ArrayList<>();

        ListOfCities startingCity = new ListOfCities();
        startingCity.setName("Starting city");
        listOfCities.add(startingCity);

        ListOfCities battleField = new ListOfCities();
        battleField.setName("Battlefield");
        listOfCities.add(battleField);

        // Save the ListOfCities first
        listOfCitiesRepository.saveAll(listOfCities);


        List<City> coordStartingCity = new ArrayList<>();
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                City city = new City();
                city.setXCoord(x);
                city.setYCoord(y);
                Random random = new Random();
                int random_number = random.nextInt(3) + 1;
                if(random_number == 3)
                {
                    city.setTerrainType("Stone");
                }else {
                    if (random_number==1){
                        Enemy enemy = enemyService.createEnemy(city);
                        city.getEnemy().add(enemy);
                        Enemy enemy1 = enemyService.createEnemy(city);
                        city.getEnemy().add(enemy1);
                    }
                    city.setTerrainType("Grass");
                }

                // Set the ListOfCities for each City
                city.setListOfCities(battleField);

                coordStartingCity.add(city);
            }
        }
        cityRepository.saveAll(coordStartingCity);

        List<City> coordBattleField = new ArrayList<>();
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                City city = new City();
                city.setXCoord(x);
                city.setYCoord(y);
                city.setTerrainType("city streets");

                // Set the ListOfCities for each City
                city.setListOfCities(startingCity);

                coordBattleField.add(city);
            }
        }

        // Save all the City entities
        cityRepository.saveAll(coordBattleField);
    }
    public City getById(Long id){
        return cityRepository.getById(id);
    }
}
