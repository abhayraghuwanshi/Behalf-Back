package com.behalf.delta.constants;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum QuestCategory {

    PHOTOGRAPHY("Photography"),
    GRAPHIC_DESIGN("Graphic Design"),
    PET_SITTING("Pet Sitting"),
    SEWING("Sewing"),
    HANDIWORK("Handiwork"),
    PICKUP_DELIVERY("Pick up and delivery"),
    BOOK_KEEPING("Book Keeping"),
    ONLINE_TUTORIAL("Online tutorial");

    private final String category;

    QuestCategory(String category) {
        this.category = category;
    }

}
