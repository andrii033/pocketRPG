package com.ta.pocketRPG.domain.model;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

@Data
@Entity
public class GameCharacter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter
    private String characterName;
    private int str=1;
    private int agi=1;
    private int inte=1;
    private int gold=1;
    private int res=0;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;

}