package com.ta.pocketRPG.service;


import com.ta.pocketRPG.domain.model.City;
import com.ta.pocketRPG.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;
    @PostConstruct
    public void initializeCity(){
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                City city=new City();
                city.setXCoord(x);
                city.setYCoord(y);
                city.setTerrainType("Grass");
                cityRepository.save(city);
            }
        }
    }
}
