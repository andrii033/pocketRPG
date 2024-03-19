package com.ta.pocketRPG.domain.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class ListOfCities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "listOfCities", cascade = CascadeType.ALL)
    private List<City> citiesNames = new ArrayList<>();
}
