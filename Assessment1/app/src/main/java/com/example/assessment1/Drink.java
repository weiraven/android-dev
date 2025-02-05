package com.example.assessment1;

public class Drink {
    double size, percentage;

    public Drink(double size, double percentage) {
        this.size = size;
        this.percentage = percentage;
    }
    public Drink() {
    }

    public double getSize() {
        return size;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}