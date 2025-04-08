package edu.uncc.assignment09.models;

public class CreditCategory {
    String name;
    int imageResourceId;
    int min;

    public CreditCategory() {
    }

    public CreditCategory(String name, int imageResource, int min) {
        this.name = name;
        this.imageResourceId = imageResource;
        this.min = min;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
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
