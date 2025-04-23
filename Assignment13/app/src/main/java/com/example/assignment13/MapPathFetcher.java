package com.example.assignment13;
import com.google.android.gms.maps.model.LatLng;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapPathFetcher {

    private static final String API_URL = "https://www.theappsdr.com/map/route";

    public interface PathCallback {
        void onPathRetrieved(List<LatLng> path);
    }
    public void fetchPath(PathCallback callback) {
        // Create OkHttpClient instance
        OkHttpClient client = new OkHttpClient();

        // Build the request
        Request request = new Request.Builder()
                .url(API_URL)
                .build();

        new Thread(() -> {
            try {

                Response response = client.newCall(request).execute();

                if (response.isSuccessful() && response.body() != null) {
                    // Parse the response
                    String responseBody = response.body().string();
                    List<LatLng> path = parsePath(responseBody);
                    callback.onPathRetrieved(path);
                } else {
                    callback.onPathRetrieved(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                callback.onPathRetrieved(null);
            }
        }).start();
    }

    private List<LatLng> parsePath(String jsonResponse) {
        List<LatLng> path = new ArrayList<>();
        try {
            // Parse the JSON response
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray pathArray = jsonObject.getJSONArray("path");

            // Iterate through the path array
            for (int i = 0; i < pathArray.length(); i++) {
                JSONObject point = pathArray.getJSONObject(i);
                double latitude = point.getDouble("latitude");
                double longitude = point.getDouble("longitude");
                path.add(new LatLng(latitude, longitude));

                // Print or use the coordinates
                System.out.println("Point " + i + ": (" + latitude + ", " + longitude + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }
}