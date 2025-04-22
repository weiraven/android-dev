package edu.uncc.assignment12.fragments.auth;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import edu.uncc.assignment12.databinding.FragmentLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {
    public LoginFragment() {
        // Required empty public constructor
    }

    FragmentLoginBinding binding;
    LoginListener mListener;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("User Login");
        auth = FirebaseAuth.getInstance();
        binding.editTextEmail.setText("twei1@charlotte.edu");
        binding.editTextPassword.setText("test123");

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.editTextEmail.getText().toString();
                String password = binding.editTextPassword.getText().toString();

                if (email.isEmpty()) {
                    binding.editTextEmail.setError("Email required");
                    return;
                }
                if (password.isEmpty()) {
                    binding.editTextPassword.setError("Password required");
                    return;
                }
                // Firebase sign‑in
                binding.progressBar.setVisibility(View.VISIBLE);
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            binding.progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                mListener.onLoginSuccessful();
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Login failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG
                                ).show();
                            }
                        });
            }
        });

        binding.buttonCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoSignUpUser();
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof LoginListener) {
            mListener = (LoginListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement LoginListener");
        }
    }

    // GPT04-mini-high suggested nulling out the binding to avoid view-leads by adding the following onDestroyView
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public interface LoginListener {
        void gotoSignUpUser();
        void onLoginSuccessful();

    }
}