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

import edu.uncc.assignment12.databinding.FragmentSignupBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpFragment extends Fragment {
    public SignUpFragment() {
        // Required empty public constructor
    }

    FragmentSignupBinding binding;
    SignUpListener mListener;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSignupBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("User Sign Up");
        auth = FirebaseAuth.getInstance();

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.gotoLogin();
            }
        });

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname = binding.editTextFirstName.getText().toString().trim();
                String lname = binding.editTextLastName.getText().toString().trim();
                String email = binding.editTextEmail.getText().toString().trim();
                String password = binding.editTextPassword.getText().toString().trim();

                if (fname.isEmpty()) {
                    binding.editTextFirstName.setError("First Name required");
                    return;
                }
                if (lname.isEmpty()) {
                    binding.editTextLastName.setError("Last Name required");
                    return;
                }
                if (email.isEmpty()) {
                    binding.editTextEmail.setError("Email required");
                    return;
                }
                if (password.length() < 6) {
                    binding.editTextPassword.setError("At least 6 chars");
                    return;
                }

                binding.progressBar.setVisibility(View.VISIBLE);

                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            binding.progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                mListener.onSignUpSuccess();
                            } else {
                                Toast.makeText(
                                        requireContext(),
                                        "Signup failed: " + task.getException().getMessage(),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        });
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SignUpListener) {
            mListener = (SignUpListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SignUpListener");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public interface SignUpListener {
        void gotoLogin();

        void onSignUpSuccess();
    }
}

