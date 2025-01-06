package com.example.dotheG.model;

import jakarta.persistence.*;

@Entity
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHAR_ID")
    private Long charId;

    private String charName;

    private int charRarity;

    private String charImageUrl;

}
