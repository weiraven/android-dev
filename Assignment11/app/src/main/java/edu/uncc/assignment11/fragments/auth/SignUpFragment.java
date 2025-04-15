package edu.uncc.assignment11.fragments.auth;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import edu.uncc.assignment11.MainActivity;
import edu.uncc.assignment11.databinding.FragmentSignupBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpFragment extends Fragment {
    public SignUpFragment() {
        // Required empty public constructor
    }
    FragmentSignupBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSignupBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("User Sign Up");

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

                if(fname.isEmpty()){
                    Toast.makeText(getContext(), "First Name is required", Toast.LENGTH_SHORT).show();
                } else if(lname.isEmpty()){
                    Toast.makeText(getContext(), "Last Name is required", Toast.LENGTH_SHORT).show();
                } else if(email.isEmpty()){
                    Toast.makeText(getContext(), "Email is required", Toast.LENGTH_SHORT).show();
                } else if(password.isEmpty()){
                    Toast.makeText(getContext(), "Password is required", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject json = new JSONObject();
                        json.put("fname", fname);
                        json.put("lname", lname);
                        json.put("email", email);
                        json.put("password", password);

                        Log.d("SignUpFragment", "Generated JSON: " + json.toString());  // Verify JSON output

                        MediaType jsonType = MediaType.get("application/json; charset=utf-8");
                        RequestBody requestBody = RequestBody.create(json.toString(), jsonType);
                        Request request = new Request.Builder()
                                .url("https://www.theappsdr.com/api/signup")
                                .post(requestBody)
                                .build();

                        // Use the shared OkHttpClient from MainActivity
                        ((MainActivity) getActivity()).getHttpClient().newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                getActivity().runOnUiThread(() ->
                                        Toast.makeText(getContext(), "Network Error!", Toast.LENGTH_SHORT).show()
                                );
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                if(response.body() != null) {
                                    String responseBody = response.body().string();
                                    Log.d("SignUpFragment", "Server response: " + responseBody);
                                    try {
                                        JSONObject jsonResponse = new JSONObject(responseBody);
                                        if ("ok".equals(jsonResponse.optString("status"))) {
                                            final String token = jsonResponse.getString("token");
                                            getActivity().runOnUiThread(() -> {
                                                ((MainActivity)getActivity()).storeToken(token);
                                            });
                                        } else {
                                            final String error = jsonResponse.optString("message", "Sign up failed");
                                            getActivity().runOnUiThread(() ->
                                                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show()
                                            );
                                        }
                                    } catch (JSONException e) {
                                        getActivity().runOnUiThread(() ->
                                                Toast.makeText(getContext(), "Invalid response format", Toast.LENGTH_SHORT).show()
                                        );
                                    }
                                } else {
                                    getActivity().runOnUiThread(() ->
                                            Toast.makeText(getContext(), "Unexpected response", Toast.LENGTH_SHORT).show()
                                    );
                                }
                            }
                        });

                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Error forming JSON", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    SignUpListener mListener;

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

    public interface SignUpListener {
        void gotoLogin();
        // void onSignUpSuccessful(); don't need this method when storeToken method in MainActivity already stores the token and replaces fragment with ToDoListFragment
    }
}