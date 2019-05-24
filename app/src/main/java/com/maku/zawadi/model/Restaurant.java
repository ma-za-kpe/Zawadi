package com.maku.zawadi.model;

public class Restaurant {

    private String name;

    public Restaurant(String name) {
        this.name = name;
    }

    public Restaurant() {

    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
