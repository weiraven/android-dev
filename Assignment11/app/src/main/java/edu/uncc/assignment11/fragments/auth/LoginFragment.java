package edu.uncc.assignment11.fragments.auth;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import edu.uncc.assignment11.MainActivity;
import edu.uncc.assignment11.databinding.FragmentLoginBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginFragment extends Fragment {
    public LoginFragment() {
        // Required empty public constructor
    }

    FragmentLoginBinding binding;
    LoginListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("User Login");
        // for faster testing login:
        binding.editTextEmail.setText("a@a.com");
        binding.editTextPassword.setText("test123");

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.editTextEmail.getText().toString();
                String password = binding.editTextPassword.getText().toString();
                if(email.isEmpty()){
                    Toast.makeText(getActivity(), "Enter Name!!", Toast.LENGTH_SHORT).show();
                } else if(password.isEmpty()){
                    Toast.makeText(getActivity(), "Enter Password!!", Toast.LENGTH_SHORT).show();
                } else {
                    requestLogin(email, password);
                }
            }
        });

        binding.buttonCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoSignUpUser();
            }
        });

    }

    // Java
    private void requestLogin(final String email, final String password) {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(json.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/api/login")
                .post(requestBody)
                .build();

        ((MainActivity) getActivity()).getHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getActivity(), "Network Error!", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.body() != null) {
                    String responseBody = response.body().string();
                    try {
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        if ("ok".equals(jsonResponse.optString("status"))) {
                            String token = jsonResponse.getString("token");
                            getActivity().runOnUiThread(() -> {
                                ((MainActivity)getActivity()).storeToken(token);
                                mListener.onLoginSuccessful();
                            });
                        } else {
                            final String error = jsonResponse.optString("message", "Login failed");
                            getActivity().runOnUiThread(() ->
                                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show()
                            );
                        }
                    } catch (JSONException e) {
                        // If the JSON parsing fails, there might be an issue with format.
                        getActivity().runOnUiThread(() ->
                                Toast.makeText(getActivity(), "Invalid response format", Toast.LENGTH_SHORT).show()
                        );
                    }
                } else {
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getActivity(), "Unexpected response", Toast.LENGTH_SHORT).show()
                    );
                }
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

    public interface LoginListener {
        void gotoSignUpUser();
        void onLoginSuccessful();

    }
}