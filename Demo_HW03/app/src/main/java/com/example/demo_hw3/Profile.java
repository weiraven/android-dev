package com.example.demo_hw3;

public class Profile {
    double weight;
    String gender;

    public Profile(double w, String g) {
        weight = w;
        gender = g;
    }

    public Profile(){

    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
