package com.example.assignment05;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.assignment05.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {
    private static final String KEY_PROFILE = "key_profile";
    private Profile currentProfile;

    public ProfileFragment() {
        // Required empty public constructor
    }
    public static ProfileFragment newInstance(Profile profile) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_PROFILE, profile);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            currentProfile = (Profile) getArguments().getSerializable(KEY_PROFILE);
        }
    }
    FragmentProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Profile");

        binding.textViewUserName.setText(currentProfile.getName());
        binding.textViewUserEmail.setText(currentProfile.getEmail());
        binding.textViewUserRole.setText(currentProfile.getRole());

        binding.buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileListener.gotoEditUser(currentProfile);
            }
        });
    }
    ProfileFragmentListener profileListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        profileListener = (ProfileFragmentListener) context;
    }

    interface ProfileFragmentListener{
        void gotoEditUser(Profile profile);
    }
}