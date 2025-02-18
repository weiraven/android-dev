package com.example.demo_hw3;

public class Drink {
    double alcohol, size;

    public Drink(double a, double s) {
        alcohol = a;
        size = s;
    }

    public Drink(){

    }

    public double getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(double alcohol) {
        this.alcohol = alcohol;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }
}
