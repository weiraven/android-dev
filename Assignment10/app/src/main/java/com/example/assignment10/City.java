package com.example.assignment10;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class City implements Serializable {
    String name, state;
    double lat, lng;

    public City() {
    }

    public City(JSONObject jsonObject) throws JSONException {
        this.name = jsonObject.getString("name");
        this.state = jsonObject.getString("state");
        this.lat = jsonObject.getDouble("lat");
        this.lng = jsonObject.getDouble("lng");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
