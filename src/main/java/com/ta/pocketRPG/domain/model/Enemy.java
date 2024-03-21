package com.ta.pocketRPG.domain.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Enemy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int str=1;
    private int agi=1;
    private int inte=1;
    private int hp=10;

    @ManyToOne
    private City city;


}
