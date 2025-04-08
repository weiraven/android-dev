package com.example.assignment10;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Forecast implements Serializable {
    String startTime, windSpeed, shortForecast;
    int temperature;
    double precipitation;

    public Forecast() {
    }

    public Forecast(JSONObject jsonObject) throws JSONException {
        this.startTime = jsonObject.getString("startTime");
        this.windSpeed = jsonObject.getString("windSpeed");
        this.shortForecast = jsonObject.getString("shortForecast");
        this.temperature = jsonObject.getInt("temperature");
        this.precipitation = jsonObject.getDouble("precipitation");
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getShortForecast() {
        return shortForecast;
    }

    public void setShortForecast(String shortForecast) {
        this.shortForecast = shortForecast;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public double getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(double precipitation) {
        this.precipitation = precipitation;
    }
}
