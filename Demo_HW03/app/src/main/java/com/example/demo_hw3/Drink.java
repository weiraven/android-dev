package com.example.demo_hw3;

import java.util.Date;

public class Drink {
    double alcohol, size;
    Date addedOn;

    public Drink(double a, double s) {
        alcohol = a;
        size = s;
        addedOn = new Date();
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

    public Date getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(Date addedOn) {
        this.addedOn = addedOn;
    }
}
