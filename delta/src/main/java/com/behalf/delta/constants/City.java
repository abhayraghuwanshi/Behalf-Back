package com.behalf.delta.constants;

public enum City {

    GURUGRAM("Gurugram"), MUMBAI("Mumbai"), GOA("Goa"), DELHI("Delhi");

    private final String cityName;


    City(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }

    @Override
    public String toString() {
        return cityName;
    }
}
