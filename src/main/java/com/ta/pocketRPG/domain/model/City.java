package com.ta.pocketRPG.domain.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Data
@Entity
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int xCoord;
    private int yCoord;
    private String terrainType;

    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    private List<GameCharacter> characters = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "list_of_cities_id")
    private ListOfCities listOfCities;
}


