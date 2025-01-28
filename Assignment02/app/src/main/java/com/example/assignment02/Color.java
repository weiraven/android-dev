package com.example.assignment02;
public class Color {
    int red;
    int green;
    int blue;

    public Color() {
    }

    public Color(int r, int g, int b) {
        this.red = r;
        this.green = g;
        this.blue = b;
    }

    public int getRedValue() {
        return this.red;
    }

    public int getGreenValue() {
        return this.green;
    }

    public int getBlueValue() {
        return this.blue;
    }

    public String getHexValue() {
        return String.format("#%02X%02X%02X", this.red, this.green, this.blue);
    }

    public String getRgbValue() {
        return String.format("(%d, %d, %d)", this.red, this.green, this.blue);
    }
}
