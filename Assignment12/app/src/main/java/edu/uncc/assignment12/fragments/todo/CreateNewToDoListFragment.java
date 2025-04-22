package edu.uncc.assignment12.fragments.todo;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import edu.uncc.assignment12.R;
import edu.uncc.assignment12.databinding.FragmentCreateNewToDoListBinding;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateNewToDoListFragment extends Fragment {
    public CreateNewToDoListFragment() {
        // Required empty public constructor
    }

    private FragmentCreateNewToDoListBinding binding;
    private CreateNewToDoListListener mListener;
    private FirebaseAuth auth;
    private FirebaseFirestore db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCreateNewToDoListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Create New List");

        // Firebase init
        auth = FirebaseAuth.getInstance();
        db   = FirebaseFirestore.getInstance();

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
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Toast.makeText(getContext(), "Not signed in", Toast.LENGTH_SHORT).show();
            return;
        }
        String uid = user.getUid();

        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("userId", uid);

        db.collection("todoLists")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(),
                            "To-Do list added successfully", Toast.LENGTH_SHORT).show();
                    mListener.onSuccessCreateNewToDoList();
                })
                .addOnFailureListener( e -> {
                    Toast.makeText(getContext(),
                            "Creation failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public interface CreateNewToDoListListener {
        void onSuccessCreateNewToDoList();
        void onCancelCreateNewToDoList();
    }
}