package com.maku.zawadi.model;

public class Restaurant {

    private String name;
    private Double rating;

    public Restaurant() {}

    public Restaurant(String name,  Double rating) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public Double getRating() {
        return rating;
    }

}
