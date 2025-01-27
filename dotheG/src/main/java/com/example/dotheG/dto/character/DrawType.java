package com.example.dotheG.dto.character;

public enum DrawType {
    RANDOM(25,0),
    ANIMAL(70,0),
    RANK_1(20,1),
    RANK_2(50,2),
    RANK_3(100,3),
    RANK_4(150,4);

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