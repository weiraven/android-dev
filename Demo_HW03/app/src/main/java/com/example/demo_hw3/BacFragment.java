package com.example.demo_hw3;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.demo_hw3.databinding.FragmentBacBinding;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BacFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BacFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BacFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BacFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BacFragment newInstance(String param1, String param2) {
        BacFragment fragment = new BacFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
                mListener.gotoSetProfile();
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
            }
        });

        if(mProfile == null){
            binding.buttonViewDrinks.setEnabled(false);
            binding.buttonAddDrink.setEnabled(false);
            binding.textViewWeightDisplay.setText("Weight: N/A");
        } else {
            binding.buttonViewDrinks.setEnabled(true);
            binding.buttonAddDrink.setEnabled(true);
            binding.textViewWeightDisplay.setText("Weight: " + mProfile.getWeight() + " (" + mProfile.getGender() + ")");
        }

        mDrinks = mListener.getAllDrinks();
    }

    BacFragmentListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (BacFragmentListener) context;
    }

    interface BacFragmentListener {
        void gotoSetProfile();
        void clearAllDrinks();

        ArrayList<Drink> getAllDrinks();
    }

}