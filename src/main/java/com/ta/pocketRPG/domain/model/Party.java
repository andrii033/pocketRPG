package com.ta.pocketRPG.domain.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Party {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String partyName;

    @OneToMany(mappedBy = "party", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<GameCharacter> member;
}
