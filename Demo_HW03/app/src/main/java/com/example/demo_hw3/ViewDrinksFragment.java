package com.example.demo_hw3;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.demo_hw3.databinding.FragmentViewDrinksBinding;

import java.util.ArrayList;

public class ViewDrinksFragment extends Fragment {


    private static final String ARG_PARAM_DRINKS = "ARG_PARAM_DRINKS";
    private ArrayList<Drink> mDrinkList;
    private int currentIndex = 0;

    public ViewDrinksFragment() {
        // Required empty public constructor
    }

    public static ViewDrinksFragment newInstance(ArrayList<Drink> drinks) {
        ViewDrinksFragment fragment = new ViewDrinksFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_DRINKS, drinks);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDrinkList = (ArrayList<Drink>) getArguments().getSerializable(ARG_PARAM_DRINKS);
        }
    }

    FragmentViewDrinksBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentViewDrinksBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("View Drinks");
        currentIndex = 0;
        displayDrink();

        binding.imageButtonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPrevious();
            }
        });

        binding.imageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayNext();
            }
        });

        binding.imageButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrinkList.remove(currentIndex);
                if(mDrinkList.size() == 0) {
                    mListener.cancelViewDrinks();
                } else {
                    displayPrevious();
                }
            }
        });

        binding.buttonCancelViewDrinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.cancelViewDrinks();
            }
        });
    }

    void displayPrevious(){
        if (currentIndex == 0) {
            currentIndex = mDrinkList.size() - 1;
        } else {
            currentIndex--;
        }
        displayDrink();
    }

    void displayNext() {
        if(currentIndex == mDrinkList.size() - 1) {
            currentIndex = 0;
        } else {
            currentIndex++;
        }
        displayDrink();
    }


    private void displayDrink(){
        Drink drink = mDrinkList.get(currentIndex);
        binding.textViewDrinksCount.setText("Drink " + (currentIndex + 1) + " out of " + mDrinkList.size());
        binding.textViewDrinkSize.setText(String.valueOf(drink.getSize() + " oz"));
        binding.textViewDrinkAlcoholPercent.setText(String.valueOf(drink.getAlcohol() + "% Alcohol"));
        binding.textViewDrinkAddedOn.setText("Added " + drink.getAddedOn().toString());
    }

    ViewDrinksFragmentListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (ViewDrinksFragmentListener) context;
    }

    interface ViewDrinksFragmentListener {
        void cancelViewDrinks();
    }
}