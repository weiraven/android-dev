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

import com.example.assessment2.databinding.FragmentSelectHeightBinding;

public class SelectHeightFragment extends Fragment {

    public SelectHeightFragment() {
        // Required empty public constructor
    }

    FragmentSelectHeightBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSelectHeightBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Select Height");

        binding.buttonCancelHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.cancelSelectHeight();
            }
        });

        binding.buttonSubmitHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int feet;
                int inches;
                try {
                    feet = Integer.parseInt(binding.editTextHeightFeet.getText().toString());
                    inches = Integer.parseInt(binding.editTextHeightInches.getText().toString());
                    mListener.submitHeight(feet, inches);
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Please enter a valid height", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    SelectHeightListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (SelectHeightListener) context;
    }

    interface SelectHeightListener {
        void cancelSelectHeight();
        void submitHeight(int feet, int inches);
    }
}