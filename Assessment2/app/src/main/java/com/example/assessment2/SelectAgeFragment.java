package com.example.assessment2;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.assessment2.databinding.FragmentSelectAgeBinding;

public class SelectAgeFragment extends Fragment {


    public SelectAgeFragment() {
        // Required empty public constructor
    }

    FragmentSelectAgeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSelectAgeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Select Age");

        binding.buttonCancelAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.cancelSelectAge();
            }
        });

        binding.buttonSubmitAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int age;
                try {
                    age = Integer.parseInt(binding.editTextEnteredAge.getText().toString());
                    mListener.submitAge(age);
                } catch (NumberFormatException e) {
                    binding.editTextEnteredAge.setError("Please enter a valid age");
                }
            }
        });
    }

    SelectAgeListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (SelectAgeListener) context;
    }

    interface SelectAgeListener {
        void cancelSelectAge();
        void submitAge(int age);
    }
}