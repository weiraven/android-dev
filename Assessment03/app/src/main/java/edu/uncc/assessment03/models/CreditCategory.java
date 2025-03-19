package edu.uncc.assessment03.models;

public class CreditCategory {
    String name;
    int imageResourceId;
    int min, max;


    public CreditCategory() {
    }

    public CreditCategory(String name, int imageResourceId, int min, int max) {
        this.name = name;
        this.imageResourceId = imageResourceId;
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public CreditCategory(String name, int imageResource) {
        this.name = name;
        this.imageResourceId = imageResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

}
