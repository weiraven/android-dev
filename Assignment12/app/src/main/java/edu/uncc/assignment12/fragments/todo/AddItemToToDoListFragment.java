package edu.uncc.assignment12.fragments.todo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

import edu.uncc.assignment12.R;
import edu.uncc.assignment12.databinding.FragmentAddItemToToDoListBinding;
import edu.uncc.assignment12.models.ToDoList;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class AddItemToToDoListFragment extends Fragment {
    private static final String ARG_PARAM_TODO_LIST = "ARG_PARAM_TODO_LIST";

    public static AddItemToToDoListFragment newInstance(ToDoList toDoList) {
        AddItemToToDoListFragment fragment = new AddItemToToDoListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_TODO_LIST, toDoList);
        fragment.setArguments(args);
        return fragment;
    }

    public AddItemToToDoListFragment() {
        // Required empty public constructor
    }

    private FragmentAddItemToToDoListBinding binding;
    private ToDoList mTodoList;
    private AddItemToListListener mListener;

    // Firebase
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTodoList = (ToDoList) getArguments().getSerializable(ARG_PARAM_TODO_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddItemToToDoListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Add Item to List");

        // Firebase init
        auth = FirebaseAuth.getInstance();
        db   = FirebaseFirestore.getInstance();

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancelAddItemToList(mTodoList);
            }
        });

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = binding.editTextName.getText().toString().trim();
                if (itemName.isEmpty()) {
                    Toast.makeText(getContext(),
                            "Item name cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Determine priority
                String priority = "Low";
                int checkedId = binding.radioGroup.getCheckedRadioButtonId();
                if (checkedId == R.id.radioButtonMedium) {
                    priority = "Medium";
                } else if (checkedId == R.id.radioButtonHigh) {
                    priority = "High";
                }

                createNewListItem(itemName, priority);
            }
        });


    }
    private void createNewListItem(String name, String priority) {
        // Guard: ensure signed in
        if (auth.getCurrentUser() == null) {
            Toast.makeText(getContext(),
                    "You must be signed in", Toast.LENGTH_SHORT).show();
            mListener.onCancelAddItemToList(mTodoList);
            return;
        }

        // Build data
        Map<String,Object> data = new HashMap<>();
        data.put("name", name);
        data.put("priority", priority);

        // Firestore path: todoLists/{listId}/items
        String listDocId = mTodoList.getDocumentId();
        db.collection("todoLists")
                .document(listDocId)
                .collection("items")
                .add(data)
                .addOnSuccessListener(docRef -> {
                    Toast.makeText(getContext(),
                            "Item added", Toast.LENGTH_SHORT).show();
                    mListener.onSuccessAddItemToList();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(),
                            "Creation failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddItemToListListener) {
            mListener = (AddItemToListListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AddItemToListListener");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public interface AddItemToListListener{
        void onSuccessAddItemToList();
        void onCancelAddItemToList(ToDoList todoList);
    }
}