package com.example.assessment2;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.assessment2.databinding.FragmentSelectGenderBinding;


public class SelectGenderFragment extends Fragment {

    public SelectGenderFragment() {
        // Required empty public constructor
    }

    FragmentSelectGenderBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSelectGenderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Select Gender");

        binding.buttonCancelGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.cancelSelectGender();
            }
        });

        binding.buttonSubmitGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gender = "Female";
                if (binding.radioButtonMale.isChecked()) {
                    gender = "Male";
                }
                mListener.submitGender(gender);
            }
        });
    }

    SelectGenderListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (SelectGenderListener) context;
    }

    interface SelectGenderListener {
        void cancelSelectGender();
        void submitGender(String gender);
    }
}