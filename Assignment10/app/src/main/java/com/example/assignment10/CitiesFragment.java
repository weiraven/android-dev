package com.example.assignment10;

import android.content.Context;
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
import android.widget.Toast;

import com.example.assignment10.databinding.CityListItemBinding;
import com.example.assignment10.databinding.FragmentCitiesBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CitiesFragment extends Fragment {


    public CitiesFragment() {
        // Required empty public constructor
    }

    FragmentCitiesBinding binding;
    private final OkHttpClient client = new OkHttpClient();
    ArrayList<City> cities = new ArrayList<>();
    CitiesAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCitiesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Cities");

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CitiesAdapter();
        binding.recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        binding.recyclerView.addItemDecoration(dividerItemDecoration);
        getCities();

    }

    void getCities(){

        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/api/cities")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    String body = response.body().string();
                    Log.d("Cities", "onResponse: " + body);
                    try {
                        JSONObject rootJson = new JSONObject(body);
                        JSONArray citiesJsonArray = rootJson.getJSONArray("cities");
                        cities.clear();

                        for (int i = 0; i < citiesJsonArray.length(); i++) {
                            JSONObject cityJsonObject = citiesJsonArray.getJSONObject(i);
                            City city = new City(cityJsonObject);
                            cities.add(city);
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Log.e("Cities", "onResponse: Failed with code " + response.code());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Show an error message to the user
                            Toast.makeText(getActivity(), "Failed to load cities. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.CityViewHolder>{

        @NonNull
        @Override
        public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CityViewHolder(CityListItemBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
            City city = cities.get(position);
            holder.setupUI(city);
        }

        @Override
        public int getItemCount() {
            return cities.size();
        }

        class CityViewHolder extends RecyclerView.ViewHolder{
            CityListItemBinding mBinding;
            City mCity;
            public CityViewHolder(CityListItemBinding vhBinding) {
                super(vhBinding.getRoot());
                mBinding = vhBinding;

                mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("Cities", "onClick: " + mCity.getName());
                        if (mCity != null && mListener != null) {
                            mListener.onCitySelected(mCity);
                        }
                    }
                });
            }

            public void setupUI(City city){
                this.mCity = city;
                mBinding.textViewCityName.setText(city.getName() + ", " + city.getState());
            }
        }
    }

    CitySelectedListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (CitySelectedListener) context;
    }

    interface CitySelectedListener{
        void onCitySelected(City city);

    }

}