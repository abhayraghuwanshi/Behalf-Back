package com.behalf.delta.constants;

import lombok.Getter;

public enum Rank {
    NOVICE("Novice", 1, 2),
    EXPLORER("Explorer", 3, 5),
    ADVENTURER("Adventurer", 6, 10),
    CHAMPION("Champion", 11, 20),
    LEGEND("Legend", 21, Integer.MAX_VALUE);

    @Getter
    private final String name;
    private final int minQuests;
    private final int maxQuests;

    Rank(String name, int minQuests, int maxQuests) {
        this.name = name;
        this.minQuests = minQuests;
        this.maxQuests = maxQuests;
    }

    public static Rank getRank(int successfulQuests) {
        for (Rank rank : Rank.values()) {
            if (successfulQuests >= rank.minQuests && successfulQuests <= rank.maxQuests) {
                return rank;
            }
        }
        return NOVICE; // Default
    }

}
