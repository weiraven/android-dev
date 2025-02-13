package com.example.assignment05;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.assignment05.databinding.FragmentCreateUserBinding;

public class CreateUserFragment extends Fragment {
    Profile profile = null;

    public CreateUserFragment() {
        // Required empty public constructor
    }

    FragmentCreateUserBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateUserBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Create User");

        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.editTextName.getText().toString();
                String email = binding.editTextEmailAddress.getText().toString();

                int selectedRoleId = binding.radioGroupRole.getCheckedRadioButtonId();
                String role = getString(R.string.student_radio_label);
                if (selectedRoleId == R.id.radioButtonEmployee) {
                    role = getString(R.string.employee_radio_label);
                } else if (selectedRoleId == R.id.radioButtonOther){
                    role = getString(R.string.other_radio_label);
                }

                if(name.isEmpty()){
                    Toast.makeText(getActivity(), "Please enter name", Toast.LENGTH_SHORT).show();
                } else if(email.isEmpty()){
                    Toast.makeText(getActivity(), "Please enter email", Toast.LENGTH_SHORT).show();
                } else {
                    Profile profile = new Profile(name, email, role);
                    createUserListener.gotoProfile(profile);
                }
            }
        });

    }

    CreateUserFragmentListener createUserListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        createUserListener = (CreateUserFragmentListener) context;
    }

    interface CreateUserFragmentListener{
        void gotoProfile(Profile profile);
    }
}