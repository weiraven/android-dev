package com.example.demo_hw3;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.example.demo_hw3.databinding.FragmentAddDrinkBinding;

public class AddDrinkFragment extends Fragment {


    public AddDrinkFragment() {
        // Required empty public constructor
    }

    FragmentAddDrinkBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddDrinkBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Add Drink");

        binding.seekBarAlcoholPercent.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.textViewAlcoholPercentDisplay.setText(progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        binding.buttonSetAddDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double alcohol = binding.seekBarAlcoholPercent.getProgress();
                double size = 1.0;
                if(binding.radioGroupDrinkSize.getCheckedRadioButtonId() == R.id.radioButtonFiveOunces){
                    size = 5.0;
                } else if(binding.radioGroupDrinkSize.getCheckedRadioButtonId() == R.id.radioButtonTwelveOunces){
                    size = 12.0;
                }

                Drink drink = new Drink(alcohol, size);
                mListener.sendDrink(drink);
            }
        });

        binding.buttonCancelAddDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.cancelAddDrink();
            }
        });

    }

    AddDrinkFragmentListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (AddDrinkFragmentListener) context;
    }

    interface AddDrinkFragmentListener{
        void sendDrink(Drink drink);
        void cancelAddDrink();
    }
}