package edu.uncc.assignment11.fragments.todo;

import android.app.Activity;
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
import edu.uncc.assignment11.R;
import edu.uncc.assignment11.databinding.FragmentCreateNewToDoListBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateNewToDoListFragment extends Fragment {
    public CreateNewToDoListFragment() {
        // Required empty public constructor
    }

    FragmentCreateNewToDoListBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCreateNewToDoListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Create New List");
        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancelCreateNewToDoList();
            }
        });

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String listName = binding.editTextName.getText().toString().trim();
                if (listName.isEmpty()) {
                    Toast.makeText(getContext(), "List name cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    createNewToDoList(listName);
                }
            }
        });
    }

    private void createNewToDoList(String name) {
        final Activity activity = getActivity();
        if (activity == null) return;

        String token = activity.getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                .getString("token", null);
        if (token == null || token.isEmpty()) {
            Toast.makeText(getContext(), "No token found", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "https://www.theappsdr.com/api/todolists/create";
        RequestBody formBody = new FormBody.Builder()
                .add("name", name)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .header("Authorization", "BEARER " + token)
                .build();

        ((MainActivity) activity).getHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                activity.runOnUiThread(() ->
                        Toast.makeText(getContext(), "Failed to create new list", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseBody = response.body().string();
                try {
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    if ("ok".equals(jsonResponse.optString("status"))) {
                        activity.runOnUiThread(() -> {
                            Toast.makeText(getContext(), "Todo list added successfully", Toast.LENGTH_SHORT).show();
                            // Pop back stack on success
                            requireActivity().getSupportFragmentManager().popBackStack();
                        });
                    } else {
                        final String error = jsonResponse.optString("message", "Creation failed");
                        activity.runOnUiThread(() ->
                                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show());
                    }
                } catch (JSONException e) {
                    activity.runOnUiThread(() ->
                            Toast.makeText(getContext(), "Error parsing response", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
    CreateNewToDoListListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CreateNewToDoListListener) {
            mListener = (CreateNewToDoListListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement CreateNewToDoListListener");
        }
    }

    public interface CreateNewToDoListListener {
        void onSuccessCreateNewToDoList();
        void onCancelCreateNewToDoList();
    }
}