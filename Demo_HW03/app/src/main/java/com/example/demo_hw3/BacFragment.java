package com.example.demo_hw3;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.demo_hw3.databinding.FragmentBacBinding;

import java.util.ArrayList;

public class BacFragment extends Fragment {

    public BacFragment() {
        // Required empty public constructor
    }

    private Profile mProfile;

    public void setProfile(Profile profile){
        this.mProfile = profile;
    }

    FragmentBacBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBacBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    ArrayList<Drink> mDrinks;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("BAC Calculator");

        binding.buttonSetWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToSetProfile();
            }
        });

        binding.buttonViewDrinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDrinks.size() > 0) {
                    mListener.goToViewDrinks();
                } else {
                    Toast.makeText(getActivity(), "No drinks to view!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.buttonAddDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToAddDrink();
            }
        });

        binding.buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.clearAllDrinks();

                binding.buttonViewDrinks.setEnabled(false);
                binding.buttonAddDrink.setEnabled(false);
                binding.textViewWeightDisplay.setText("Weight: N/A");
                binding.textViewNumDrinksDisplay.setText("# Drinks: 0");
                binding.textViewBACLevelDisplay.setText("BAC Level: 0.000");
                binding.textViewStatusDisplay.setText("You're safe");
                binding.viewStatusColor.setBackgroundResource(R.color.safe_color);
            }
        });

        mDrinks = mListener.getAllDrinks();

        if(mProfile == null){
            binding.buttonViewDrinks.setEnabled(false);
            binding.buttonAddDrink.setEnabled(false);
            binding.textViewWeightDisplay.setText("Weight: N/A");
            binding.textViewNumDrinksDisplay.setText("# Drinks: 0");
            binding.textViewBACLevelDisplay.setText("BAC Level: 0.000");
            binding.textViewStatusDisplay.setText("You're safe");
            binding.viewStatusColor.setBackgroundResource(R.color.safe_color);
        } else {
            binding.buttonViewDrinks.setEnabled(true);
            binding.buttonAddDrink.setEnabled(true);
            binding.textViewWeightDisplay.setText("Weight: " + mProfile.getWeight() + " (" + mProfile.getGender() + ")");
            displayBAC();
        }
    }

    private void displayBAC(){
        binding.textViewNumDrinksDisplay.setText("# Drinks: " + mDrinks.size());
        double value_r = 0.66;
        if(mProfile.getGender().equals("Male")){
            value_r = 0.73;
        }

        double value_a = 0.0;
        for (Drink drink: mDrinks) {
            value_a += drink.getAlcohol() * drink.getSize() / 100.00;
        }

        double bac = value_a * 5.14 / (mProfile.getWeight() * value_r);
        binding.textViewBACLevelDisplay.setText(String.format("BAC Level: %.3f",bac));

        if(bac <= 0.08){
            binding.textViewStatusDisplay.setText("You're safe");
            binding.viewStatusColor.setBackgroundResource(R.color.safe_color);
        } else if (bac <= 0.2) {
            binding.textViewStatusDisplay.setText("Be careful");
            binding.viewStatusColor.setBackgroundResource(R.color.be_careful_color);
        } else {
            binding.textViewStatusDisplay.setText("Over the limit!");
            binding.viewStatusColor.setBackgroundResource(R.color.over_limit_color);
        }
    }
    BacFragmentListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (BacFragmentListener) context;
    }

    interface BacFragmentListener {
        void goToSetProfile();
        void clearAllDrinks();
        ArrayList<Drink> getAllDrinks();
        void goToAddDrink();
        void goToViewDrinks();

    }

}