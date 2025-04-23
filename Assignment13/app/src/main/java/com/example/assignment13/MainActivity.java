package com.example.assignment13;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

// Implement OnMapReadyCallback.
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout file as the content view.
        setContentView(R.layout.activity_main);

        // Get a handle to the fragment and register the callback.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    // Get a handle to the GoogleMap object and display marker.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        fetchAndPlotPath();
    }

    private void fetchAndPlotPath() {
        MapPathFetcher fetcher = new MapPathFetcher();
        fetcher.fetchPath(new MapPathFetcher.PathCallback() {
            @Override
            public void onPathRetrieved(List<LatLng> path) {
                if (path == null || path.isEmpty()) {
                    System.err.println("Path data is null or empty.");
                    return;
                }

                runOnUiThread(() -> {
                    // Draw the polyline
                    PolylineOptions polylineOptions = new PolylineOptions().addAll(path);
                    mMap.addPolyline(polylineOptions);

                    // Add markers for start and end points
                    LatLng startPoint = path.get(0);
                    LatLng endPoint = path.get(path.size() - 1);

                    mMap.addMarker(new MarkerOptions().position(startPoint).title("Start Point"));
                    mMap.addMarker(new MarkerOptions().position(endPoint).title("End Point"));

                    // Create LatLngBounds to include all points
                    LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
                    for (LatLng point : path) {
                        boundsBuilder.include(point);
                    }
                    LatLngBounds bounds = boundsBuilder.build();

                    // Move and zoom the camera to fit the bounds
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));

                    System.out.println("Markers, polyline, and camera update completed.");
                });
            }
        });
    }
}