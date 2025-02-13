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

import com.example.assignment05.databinding.FragmentEditUserBinding;


public class EditUserFragment extends Fragment {

    private static final String KEY_PROFILE = "key_profile";

    public EditUserFragment() {
        // Required empty public constructor
    }

    FragmentEditUserBinding binding;

    public static EditUserFragment newInstance(Profile profile) {
        EditUserFragment fragment = new EditUserFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_PROFILE, profile);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditUserBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Edit User");

        binding.buttonEditCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUserListener.cancelEdit();
            }
        });

        binding.buttonEditSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedName = binding.editTextEditName.getText().toString();
                String updatedEmail = binding.editTextEditEmail.getText().toString();
                String updatedRole = "";

                int selectedId = binding.radioGroupEditRole.getCheckedRadioButtonId();
                if (selectedId == R.id.radioButtonStudentEdit) {
                    updatedRole = "Student";
                } else if (selectedId == R.id.radioButtonEmployeeEdit) {
                    updatedRole = "Employee";
                } else if (selectedId == R.id.radioButtonOtherEdit) {
                    updatedRole = "Other";
                }

                if(updatedName.isEmpty()){
                    Toast.makeText(getActivity(), "Please enter name", Toast.LENGTH_SHORT).show();
                } else if(updatedEmail.isEmpty()){
                    Toast.makeText(getActivity(), "Please enter email", Toast.LENGTH_SHORT).show();
                } else {
                    Profile updatedProfile = new Profile(updatedName, updatedEmail, updatedRole);
                    editUserListener.sendUpdatedProfile(updatedProfile);
                }
            }
        });
    }

    EditUserFragmentListener editUserListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        editUserListener = (EditUserFragmentListener) context;
    }

    interface EditUserFragmentListener{
        void sendUpdatedProfile(Profile profile);
        void cancelEdit();
    }

}