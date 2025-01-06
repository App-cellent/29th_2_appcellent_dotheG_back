package com.example.dotheG.dto;

public enum DrawType {
    RANDOM(20,0),
    ANIMAL(35,0),
    RANK_1(19,1),
    RANK_2(25,2),
    RANK_3(35,3),
    RANK_4(55,4);

    private final int cost; // 뽑기 비용
    private final int rarity; // 희귀도

    DrawType(int cost, int rarity) {
        this.cost = cost;
        this.rarity = rarity;
    }

    public int getCost() {
        return cost;
    }

    public int getRarity() {
        return rarity;
    }
}