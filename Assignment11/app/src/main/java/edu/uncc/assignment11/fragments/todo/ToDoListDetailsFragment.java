package edu.uncc.assignment11.fragments.todo;

import android.app.Activity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import edu.uncc.assignment11.MainActivity;
import edu.uncc.assignment11.R;
import edu.uncc.assignment11.databinding.FragmentListDetailsBinding;
import edu.uncc.assignment11.databinding.ListItemListItemBinding;
import edu.uncc.assignment11.models.ToDoList;
import edu.uncc.assignment11.models.ToDoListItem;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ToDoListDetailsFragment extends Fragment {
    private static final String ARG_PARAM_TODO_LIST= "ARG_PARAM_TODO_LIST";
    FragmentListDetailsBinding binding;
    private ToDoList mToDoList;

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

    ArrayList<ToDoListItem> mToDoListItems = new ArrayList<>();
    ToDoListItemAdapter adapter;

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
        loadToDoListItems();
    }

    void loadToDoListItems(){
        final Activity activity = getActivity();
        if (activity == null) return;

        String token = activity.getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                .getString("token", null);
        if(token == null || token.isEmpty()){
            activity.runOnUiThread(() ->
                    Toast.makeText(getContext(), "No token found", Toast.LENGTH_SHORT).show());
            return;
        }

        String url = "https://www.theappsdr.com/api/todolists/" + mToDoList.getTodolistId();
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "BEARER " + token)
                .build();

        ((MainActivity) activity).getHttpClient().newCall(request).enqueue(new Callback(){
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Activity act = getActivity();
                if(act != null){
                    act.runOnUiThread(() ->
                            Toast.makeText(getContext(), "Failed to load list items", Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Activity act = getActivity();
                if(act == null) return;
                okhttp3.ResponseBody responseBodyObj = response.body();
                if(responseBodyObj == null){
                    act.runOnUiThread(() ->
                            Toast.makeText(getContext(), "Empty response", Toast.LENGTH_SHORT).show());
                    return;
                }
                String responseBody = responseBodyObj.string();
                try {
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    if("ok".equals(jsonResponse.optString("status"))){
                        JSONObject todoListObj = jsonResponse.getJSONObject("todolist");
                        JSONArray itemsArray = todoListObj.getJSONArray("items");
                        mToDoListItems.clear();
                        for (int i = 0; i < itemsArray.length(); i++){
                            JSONObject itemObj = itemsArray.getJSONObject(i);
                            int itemId = itemObj.getInt("todolist_item_id");
                            String name = itemObj.getString("name");
                            String priority = itemObj.getString("priority");
                            ToDoListItem item = new ToDoListItem(itemId, name, priority);
                            mToDoListItems.add(item);
                        }
                        act.runOnUiThread(() -> adapter.notifyDataSetChanged());
                    }
                    else{
                        final String error = jsonResponse.optString("message", "Failed to load items");
                        act.runOnUiThread(() ->
                                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show());
                    }
                } catch (JSONException e) {
                    act.runOnUiThread(() ->
                            Toast.makeText(getContext(), "Error parsing response", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    void deleteToDoListItem(final ToDoListItem toDoListItem){
        new AlertDialog.Builder(getContext())
                .setTitle("Delete ToDo Item")
                .setMessage("Are you sure you want to delete this ToDo Item?")
                .setPositiveButton("OK", (dialog, which) -> {
                    Activity activity = getActivity();
                    if (activity == null) return;

                    String token = activity.getSharedPreferences(
                                    getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                            .getString("token", null);
                    if (token == null || token.isEmpty()){
                        Toast.makeText(getContext(), "No token found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String url = "https://www.theappsdr.com/api/todolist-items/delete";
                    RequestBody formBody = new FormBody.Builder()
                            .add("todolist_id", String.valueOf(mToDoList.getTodolistId()))
                            .add("todolist_item_id", String.valueOf(toDoListItem.getItemId()))
                            .build();
                    Request request = new Request.Builder()
                            .url(url)
                            .post(formBody)
                            .header("Authorization", "BEARER " + token)
                            .build();

                    ((MainActivity) activity).getHttpClient().newCall(request).enqueue(new Callback(){
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            Activity act = getActivity();
                            if(act != null){
                                act.runOnUiThread(() ->
                                        Toast.makeText(getContext(), "Failed to delete item", Toast.LENGTH_SHORT).show());
                            }
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            Activity act = getActivity();
                            if(act == null) return;
                            String responseBody = response.body().string();
                            try {
                                JSONObject jsonResponse = new JSONObject(responseBody);
                                if("ok".equals(jsonResponse.optString("status"))){
                                    act.runOnUiThread(() -> {
                                        Toast.makeText(getContext(), "ToDo Item deleted", Toast.LENGTH_SHORT).show();
                                        // Reload the list items upon successful deletion.
                                        loadToDoListItems();
                                    });
                                }
                                else{
                                    final String error = jsonResponse.optString("message", "Deletion failed");
                                    act.runOnUiThread(() ->
                                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show());
                                }
                            } catch (JSONException e) {
                                act.runOnUiThread(() ->
                                        Toast.makeText(getContext(), "Error parsing deletion response", Toast.LENGTH_SHORT).show());
                            }
                        }
                    });
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

    ToDoListDetailsListener mListener;

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

    public interface ToDoListDetailsListener {
        void gotoAddListItem(ToDoList toDoList);
        void goBackToToDoLists();
    }
}