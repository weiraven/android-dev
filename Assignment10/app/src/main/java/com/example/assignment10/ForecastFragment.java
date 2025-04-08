package com.example.assignment10;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.assignment10.databinding.ForecastListItemBinding;
import com.example.assignment10.databinding.FragmentCitiesBinding;
import com.example.assignment10.databinding.FragmentForecastBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ForecastFragment extends Fragment {

    private static final String ARG_LATITUDE = "latitude";
    private static final String ARG_LONGITUDE = "longitude";


    public ForecastFragment() {
        // Required empty public constructor
    }

    FragmentForecastBinding binding;
    private final OkHttpClient client = new OkHttpClient();
    ArrayList<Forecast> forecastList;
    ForecastAdapter adapter;

    public static ForecastFragment newInstance(City city) {
        ForecastFragment fragment = new ForecastFragment();
        Bundle args = new Bundle();
        args.putSerializable("city", city);
        args.putDouble(ARG_LATITUDE, city.getLat());
        args.putDouble(ARG_LONGITUDE, city.getLng());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            double latitude = getArguments().getDouble(ARG_LATITUDE);
            double longitude = getArguments().getDouble(ARG_LONGITUDE);
            fetchForecast(latitude, longitude);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentForecastBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Forecast");

        if (getArguments() != null) {
            City city = (City) getArguments().getSerializable("city");
            if (city != null) {
                binding.textViewForecastTitle.setText(city.getName() + ", " + city.getState());
            }
        }

        forecastList = new ArrayList<>();
        adapter = new ForecastAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        binding.recyclerView.setAdapter(adapter);
    }

    void fetchForecast(double latitude, double longitude){
        String url = String.format("https://api.weather.gov/points/%f,%f", latitude, longitude);

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("ForecastFragment", "Failed to fetch forecast", e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    try {
                        JSONObject forecastJson = new JSONObject(responseBody);
                        JSONObject properties = forecastJson.getJSONObject("properties");
                        String forecastUrl = properties.getString("forecast");
                        Log.d("ForecastFragment", "Forecast URL: " + forecastUrl);
                        fetchForecastDetails(forecastUrl);

                    } catch (JSONException e) {
                        Log.e("ForecastFragment", "Failed to parse forecast JSON", e);
                    }
                } else {
                    Log.e("ForecastFragment", "Failed with response code: " + response.code());
                }
            }
        });

    }

    void fetchForecastDetails(String forecastUrl) {
        Request request = new Request.Builder()
                .url(forecastUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("ForecastFragment", "Failed to fetch forecast details", e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    try {
                        JSONObject forecastJson = new JSONObject(responseBody);
                        JSONObject properties = forecastJson.getJSONObject("properties");

                        forecastList.clear();
                        for (int i = 0; i < properties.getJSONArray("periods").length(); i++) {
                            JSONObject period = properties.getJSONArray("periods").getJSONObject(i);

                            String startTime = period.getString("startTime");
                            int temperature = period.getInt("temperature");
                            String windSpeed = period.getString("windSpeed");
                            double precipitation = 0.00;

                            if (period.has("probabilityOfPrecipitation") && !period.isNull("probabilityOfPrecipitation")) {
                                JSONObject precipitationObject = period.getJSONObject("probabilityOfPrecipitation");
                                if (precipitationObject.has("value") && !precipitationObject.isNull("value")) {
                                    precipitation = precipitationObject.getDouble("value");
                                }
                            }

                            String shortForecast = period.getString("shortForecast");

                            Forecast forecast = new Forecast();
                            forecast.setStartTime(startTime);
                            forecast.setTemperature(temperature);
                            forecast.setWindSpeed(windSpeed);
                            forecast.setPrecipitation(precipitation);
                            forecast.setShortForecast(shortForecast);

                            forecastList.add(forecast);
                        }

                        Log.d("ForecastFragment", "Forecast List: " + forecastList.size() + " items");

                        getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());

                    } catch (JSONException e) {
                        Log.e("ForecastFragment", "Failed to parse forecast details JSON", e);
                    }
                } else {
                    Log.e("ForecastFragment", "Failed with response code: " + response.code());
                }
            }
        });
    }

    class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>{

        @NonNull
        @Override
        public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new ForecastViewHolder(ForecastListItemBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
            Forecast forecast = forecastList.get(position);
            holder.setupUI(forecast);
        }

        @Override
        public int getItemCount() {
            return forecastList.size();
        }

        class ForecastViewHolder extends RecyclerView.ViewHolder{
            ForecastListItemBinding mBinding;
            Forecast mForecast;
            public ForecastViewHolder(ForecastListItemBinding vhBinding) {
                super(vhBinding.getRoot());
                mBinding = vhBinding;
            }

            public void setupUI(Forecast forecast){
                this.mForecast = forecast;

                String formattedStartTime = formatDate(forecast.getStartTime());
                mBinding.textViewStartTimeDisplay.setText(formattedStartTime);

                mBinding.textViewTemperatureDisplay.setText(forecast.getTemperature() + "°F");
                mBinding.textViewPrecipitationDisplay.setText("Precipitation: " + forecast.getPrecipitation() + "%");
                mBinding.textViewWindDisplay.setText("Wind Speed: " + forecast.getWindSpeed());
                mBinding.textViewWeatherDisplay.setText(forecast.getShortForecast());
            }

            private String formatDate(String startTime) {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());
                try {
                    Date date = inputFormat.parse(startTime);
                    return outputFormat.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return startTime; // Return the original string if parsing fails
                }
            }

        }
    }
}