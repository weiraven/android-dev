package edu.uncc.assignment12.fragments.todo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.uncc.assignment12.R;
import edu.uncc.assignment12.databinding.FragmentListDetailsBinding;
import edu.uncc.assignment12.databinding.ListItemListItemBinding;
import edu.uncc.assignment12.models.ToDoList;
import edu.uncc.assignment12.models.ToDoListItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;

public class ToDoListDetailsFragment extends Fragment {
    private static final String ARG_PARAM_TODO_LIST= "ARG_PARAM_TODO_LIST";

    public static ToDoListDetailsFragment newInstance(ToDoList toDoList) {
        ToDoListDetailsFragment fragment = new ToDoListDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_TODO_LIST, toDoList);
        fragment.setArguments(args);
        return fragment;
    }

    public ToDoListDetailsFragment() {
        // Required empty public constructor
    }

    private FragmentListDetailsBinding binding;
    private ToDoList mToDoList;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private ListenerRegistration itemsListener;
    private ToDoListDetailsListener mListener;

    private ArrayList<ToDoListItem> mToDoListItems = new ArrayList<>();
    private ToDoListItemAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mToDoList = (ToDoList) getArguments().getSerializable(ARG_PARAM_TODO_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("ToDo Lists");
        // Firebase init
        auth = FirebaseAuth.getInstance();
        db   = FirebaseFirestore.getInstance();

        // Guard: ensure user is signed in
        if (auth.getCurrentUser() == null) {
            Toast.makeText(getContext(), "Not signed in", Toast.LENGTH_SHORT).show();
            // pop back to list
            mListener.goBackToToDoLists();
            return;
        }

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.todo_list_details_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.add_new_list_item_action){
                    mListener.gotoAddListItem(mToDoList);
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goBackToToDoLists();
            }
        });

        adapter = new ToDoListItemAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        listenToItems(mToDoList.getDocumentId());
    }

    private void deleteToDoListItem(final ToDoListItem toDoListItem) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete ToDo Item")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("OK", (d, w) -> {
                    String listDocId = mToDoList.getDocumentId();
                    String itemDocId = toDoListItem.getDocumentId();

                    db.collection("todoLists")
                            .document(listDocId)
                            .collection("items")
                            .document(itemDocId)
                            .delete()
                            .addOnSuccessListener(v ->
                                    Toast.makeText(getContext(),"Item deleted",Toast.LENGTH_SHORT).show()
                            )
                            .addOnFailureListener(e ->
                                    Toast.makeText(getContext(),
                                            "Deletion failed: " + e.getMessage(),
                                            Toast.LENGTH_SHORT).show()
                            );
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    class ToDoListItemAdapter extends RecyclerView.Adapter<ToDoListItemAdapter.ToDoListItemViewHolder>{

        @NonNull
        @Override
        public ToDoListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ListItemListItemBinding itemBinding = ListItemListItemBinding.inflate(getLayoutInflater(), parent, false);
            return new ToDoListItemViewHolder(itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull ToDoListItemViewHolder holder, int position) {
            ToDoListItem toDoListItem = mToDoListItems.get(position);
            holder.bind(toDoListItem);
        }

        @Override
        public int getItemCount() {
            return mToDoListItems.size();
        }

        class ToDoListItemViewHolder extends RecyclerView.ViewHolder{
            ListItemListItemBinding itemBinding;
            ToDoListItem mToDoListItem;
            public ToDoListItemViewHolder(ListItemListItemBinding itemBinding) {
                super(itemBinding.getRoot());
                this.itemBinding = itemBinding;
            }

            public void bind(ToDoListItem toDoListItem) {
                this.mToDoListItem = toDoListItem;

                itemBinding.imageViewDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteToDoListItem(mToDoListItem);
                    }
                });

                itemBinding.textViewName.setText(toDoListItem.getName());
                itemBinding.textViewPriority.setText(toDoListItem.getPriority());
            }
        }
    }

    private void listenToItems(String listDocId) {
        // remove any existing listener
        if (itemsListener != null) {
            itemsListener.remove();
        }

        itemsListener = db.collection("todoLists")
                .document(listDocId)
                .collection("items")
                .addSnapshotListener((snap, err) -> {
                    if (err != null) {
                        Toast.makeText(getContext(),
                                "Error loading items: " + err.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mToDoListItems.clear();
                    for (QueryDocumentSnapshot doc : snap) {
                        ToDoListItem item = new ToDoListItem();
                        item.setDocumentId(doc.getId());
                        item.setName(doc.getString("name"));
                        item.setPriority(doc.getString("priority"));
                        mToDoListItems.add(item);
                    }
                    adapter.notifyDataSetChanged();
                });
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ToDoListDetailsListener) {
            mListener = (ToDoListDetailsListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ToDoListDetailsListener");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (itemsListener != null) {
            itemsListener.remove();
            itemsListener = null;
        }
        binding = null;
    }


    public interface ToDoListDetailsListener {
        void gotoAddListItem(ToDoList toDoList);
        void goBackToToDoLists();
    }
}