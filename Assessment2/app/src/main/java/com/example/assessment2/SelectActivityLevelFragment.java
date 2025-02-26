package com.example.assessment2;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.assessment2.databinding.FragmentSelectActivityLevelBinding;

public class SelectActivityLevelFragment extends Fragment {

    public SelectActivityLevelFragment() {
        // Required empty public constructor
    }

    FragmentSelectActivityLevelBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSelectActivityLevelBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Select Activity Level");

        binding.buttonCancelActivityLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.cancelSelectActivityLevel();
            }
        });

        binding.buttonSedentary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String activityLevel = "Sedentary";
                mListener.submitActivityLevel(activityLevel);
            }
        });

        binding.buttonLightlyActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String activityLevel = "Lightly Active";
                mListener.submitActivityLevel(activityLevel);
            }
        });

        binding.buttonModeratelyActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String activityLevel = "Moderately Active";
                mListener.submitActivityLevel(activityLevel);
            }
        });

        binding.buttonVeryActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String activityLevel = "Very Active";
                mListener.submitActivityLevel(activityLevel);
            }
        });

        binding.buttonSuperActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String activityLevel = "Super Active";
                mListener.submitActivityLevel(activityLevel);
            }
        });
    }

    SelectActivityLevelListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (SelectActivityLevelListener) context;
    }

    interface SelectActivityLevelListener {
        void cancelSelectActivityLevel();
        void submitActivityLevel(String activityLevel);
    }
}