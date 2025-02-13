package com.example.assignment05;

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

    private Profile mProfile;
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
            mProfile = (Profile) getArguments().getSerializable(KEY_PROFILE);
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

        binding.textViewUserName.setText(mProfile.getName());
        binding.textViewUserEmail.setText(mProfile.getEmail());
        binding.textViewUserRole.setText(mProfile.getRole());
    }
    interface ProfileFragmentListener{
        void gotoEditUser();
    }
}