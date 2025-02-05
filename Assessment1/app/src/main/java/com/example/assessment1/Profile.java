package com.example.assessment1;

public class Profile {
    private String gender;
    private double weight;
    public Profile(String gender, double weight) {
        this.gender = gender;
        this.weight = weight;
    }
    public String getGender() {
        return this.gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public double getWeight() {
        return this.weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }
}