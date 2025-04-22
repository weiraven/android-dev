package edu.uncc.assignment12.fragments.todo;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import edu.uncc.assignment12.R;
import edu.uncc.assignment12.databinding.FragmentToDoListsBinding;
import edu.uncc.assignment12.databinding.ListItemTodoListBinding;
import edu.uncc.assignment12.models.ToDoList;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;



public class ToDoListsFragment extends Fragment {
    public ToDoListsFragment() {
        // Required empty public constructor
    }

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private ListenerRegistration todoListsListener;
    private ArrayList<ToDoList> mToDoLists = new ArrayList<>();
    private ToDoListAdapter adapter;
    private FragmentToDoListsBinding binding;

    private ToDoListsListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentToDoListsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("ToDo Lists");
        auth = FirebaseAuth.getInstance();
        db   = FirebaseFirestore.getInstance();

        // Start listening to this user’s to‑do lists
        String uid = auth.getCurrentUser().getUid();

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.todo_lists_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.add_new_todo_list_action){
                    mListener.gotoCreateNewToDoList();
                    return true;
                } else if(menuItem.getItemId() == R.id.logout_action){
                    mListener.logout();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        adapter = new ToDoListAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        listenToToDoLists(uid);

    }

    private void listenToToDoLists(String uid) {
        // Stop any previous listener
        if (todoListsListener != null) {
            todoListsListener.remove();
        }

        todoListsListener = db.collection("todoLists")
                .whereEqualTo("userId", uid)
                .addSnapshotListener((snap, err) -> {
                    if (err != null) {
                        Toast.makeText(getContext(),
                                "Error loading lists: " + err.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    mToDoLists.clear();
                    for (QueryDocumentSnapshot doc : snap) {
                        ToDoList t = new ToDoList();
                        t.setDocumentId(doc.getId());
                        t.setName(doc.getString("name"));
                        // you may also want t.setDocumentId(doc.getId()) if you add that field
                        mToDoLists.add(t);
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    private void deleteToDoList(ToDoList toDoList) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete ToDo List")
                .setMessage("Are you sure you want to delete this ToDo List?")
                .setPositiveButton("OK", (dialog, which) -> {
                    // Assumes you've stored the Firestore document ID in your model:
                    db.collection("todoLists")
                            .document(toDoList.getDocumentId())
                            .delete()
                            .addOnSuccessListener(v ->
                                    Toast.makeText(getContext(),
                                            "Deleted", Toast.LENGTH_SHORT).show()
                            )
                            .addOnFailureListener(e ->
                                    Toast.makeText(getContext(),
                                            "Delete failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                            );
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ToDoListViewHolder>{

        @NonNull
        @Override
        public ToDoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ListItemTodoListBinding itemBinding = ListItemTodoListBinding.inflate(getLayoutInflater(), parent, false);
            return new ToDoListViewHolder(itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull ToDoListViewHolder holder, int position) {
            ToDoList toDoList = mToDoLists.get(position);
            holder.bind(toDoList);
        }

        @Override
        public int getItemCount() {
            return mToDoLists.size();
        }

        class ToDoListViewHolder extends RecyclerView.ViewHolder{
            ListItemTodoListBinding itemBinding;
            ToDoList mToDoList;
            public ToDoListViewHolder(ListItemTodoListBinding itemBinding) {
                super(itemBinding.getRoot());
                this.itemBinding = itemBinding;
            }

            public void bind(ToDoList toDoList) {
                mToDoList = toDoList;
                itemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.gotoToDoListDetails(toDoList);
                    }
                });

                itemBinding.imageViewDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteToDoList(mToDoList);
                    }
                });

                itemBinding.textViewName.setText(toDoList.getName());
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ToDoListsListener) {
            mListener = (ToDoListsListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ToDoListsListener");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (todoListsListener != null) {
            todoListsListener.remove();
            todoListsListener = null;
        }
        binding = null;
    }

    public interface ToDoListsListener {
        void gotoCreateNewToDoList();
        void gotoToDoListDetails(ToDoList toDoList);
        void logout();
    }
}