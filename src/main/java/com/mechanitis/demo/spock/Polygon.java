package com.mechanitis.demo.spock;

public class Polygon {

    public final int numberOfSides;

    Polygon(int numberOfSides) {
        if (numberOfSides <= 2) {
            throw new TooFewSidesException("The shape must have more than 2 sides", numberOfSides);
        }
        this.numberOfSides = numberOfSides;
    }

    public int getNumberOfSides() {
        return numberOfSides;
    }
}
