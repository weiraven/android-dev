package com.example.assignment07;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.assignment07.databinding.FragmentGenresBinding;

import java.util.ArrayList;

public class GenresFragment extends Fragment {

    public GenresFragment() {
        // Required empty public constructor
    }

    FragmentGenresBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGenresBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    ArrayAdapter<String> adapter;
    ArrayList<String> genres;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Genres");

        genres = Data.getAllGenres();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, genres);
        binding.listViewGenres.setAdapter(adapter);

        binding.listViewGenres.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String genre = genres.get(position);
                mListener.sendSelectedGenre(genre);
            }
        });
    }

    GenresListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (GenresListener) context;
    }

    interface GenresListener {
        void sendSelectedGenre(String genre);
    }

}