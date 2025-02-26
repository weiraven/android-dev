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

import com.example.assessment2.databinding.FragmentCalorieCalculatorBinding;

public class CalorieCalculatorFragment extends Fragment {

    public CalorieCalculatorFragment() {
        // Required empty public constructor
    }

    FragmentCalorieCalculatorBinding binding;
    String gender;
    double weight;
    int heightFeet;
    int heightInches;
    int age;
    String activityLevel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCalorieCalculatorBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Calorie Calculator");

        binding.buttonGenderSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToSelectGender();
            }
        });

        binding.buttonWeightSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToSelectWeight();
            }
        });

        binding.buttonHeightSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToSelectHeight();
            }
        });

        binding.buttonAgeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToSelectAge();
            }
        });

        binding.buttonActivityLevelSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToSelectActivityLevel();
            }
        });

        if (gender != null) {
            binding.textViewGenderDisplay.setText("Gender: " + gender);
        }

        if (weight != 0) {
            binding.textViewWeightDisplay.setText("Weight (lbs): " + weight);
        }

        if (heightFeet != 0 && heightInches != 0) {
            binding.textViewHeightDisplay.setText("Height (feet): " + heightFeet + " ft " + heightInches + " in");
        }

        if (age != 0) {
            binding.textViewAgeDisplay.setText("Age: " + age);
        }

        if (activityLevel != null) {
            binding.textViewActivityLevelDisplay.setText("Activity Level: " + activityLevel);
        }

        binding.buttonCalCalcSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gender == null){
                    Toast.makeText(getContext(), "Please select gender", Toast.LENGTH_SHORT).show();
                } else if(weight == 0){
                    Toast.makeText(getContext(), "Please select weight", Toast.LENGTH_SHORT).show();
                } else if(heightFeet == 0 && heightInches == 0){
                    Toast.makeText(getContext(), "Please select height", Toast.LENGTH_SHORT).show();
                } else if(age == 0){
                    Toast.makeText(getContext(), "Please select age", Toast.LENGTH_SHORT).show();
                } else if(activityLevel == null){
                    Toast.makeText(getContext(), "Please select activity level", Toast.LENGTH_SHORT).show();
                } else {
                    Calorie details = new Calorie(gender, weight, heightFeet, heightInches, age, activityLevel);
                    mListener.submitDetails(details);
                    hideSelectButtons();
                    binding.textViewBMRinfo.setVisibility(View.VISIBLE);
                    binding.textViewBMRinfo.setText(String.format("Total Daily Energy Expenditure\nAt Rest (BMR): %.2f kcal/day\nAt Activity Level (TDEE): %.2f kcal/day", getRestBMR(), getActiveBMR()));
                }
            }
        });

    }

    private double getRestBMR(){
        double restBMR;
        if (gender.equals("Male")) {
            restBMR = (10 * weight)/2.205 + (15.875 * (heightFeet * 12 + heightInches)) - (5 * age) + 5;
        } else {
            restBMR = (10 * weight)/2.205 + (15.875 * (heightFeet * 12 + heightInches)) - (5 * age) - 161;
        }
        return restBMR;
    }

    private double getActiveBMR(){
        double activeBMR;
        if (activityLevel.equals("Sedentary")) {
            activeBMR = getRestBMR() * 1.2;
        } else if (activityLevel.equals("Lightly Active")) {
            activeBMR = getRestBMR() * 1.375;
        } else if (activityLevel.equals("Moderately Active")) {
            activeBMR = getRestBMR() * 1.55;
        } else if (activityLevel.equals("Very Active")) {
            activeBMR = getRestBMR() * 1.725;
        } else {
            activeBMR = getRestBMR() * 1.9;
        }
        return activeBMR;
    }
    private void hideSelectButtons() {
        binding.buttonGenderSelect.setVisibility(View.GONE);
        binding.buttonWeightSelect.setVisibility(View.GONE);
        binding.buttonHeightSelect.setVisibility(View.GONE);
        binding.buttonAgeSelect.setVisibility(View.GONE);
        binding.buttonActivityLevelSelect.setVisibility(View.GONE);
    }
    CalorieCalculatorListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (CalorieCalculatorListener) context;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setHeight(int heightFeet, int heightInches) {
        this.heightFeet = heightFeet;
        this.heightInches = heightInches;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setActivityLevel(String activityLevel) {
        this.activityLevel = activityLevel;
    }

    public void setDetails(Calorie details) {
        this.gender = details.getGender();
        this.weight = details.getWeight();
        this.heightFeet = details.getHeightFeet();
        this.heightInches = details.getHeightInches();
        this.age = details.getAge();
        this.activityLevel = details.getActivityLevel();
    }

    interface CalorieCalculatorListener {
        void goToSelectGender();
        void goToSelectWeight();
        void goToSelectHeight();
        void goToSelectAge();
        void goToSelectActivityLevel();
        void submitDetails(Calorie details);

    }
}