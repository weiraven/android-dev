package com.example.assessment2;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.assessment2.databinding.FragmentSelectWeightBinding;

public class SelectWeightFragment extends Fragment {

    public SelectWeightFragment() {
        // Required empty public constructor
    }

    FragmentSelectWeightBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSelectWeightBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Select Weight");

        binding.buttonCancelWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.cancelSelectWeight();
            }
        });

        binding.buttonSubmitWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double weight;
                try {
                    weight = Double.parseDouble(binding.editTextEnteredWeight.getText().toString());
                    mListener.submitWeight(weight);
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Please enter a valid weight", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    SelectWeightListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (SelectWeightListener) context;
    }

    interface SelectWeightListener {
        void cancelSelectWeight();
        void submitWeight(double weight);
    }
}