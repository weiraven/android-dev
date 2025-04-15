package edu.uncc.assignment11.fragments.todo;

import android.app.Activity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import edu.uncc.assignment11.MainActivity;
import edu.uncc.assignment11.R;
import edu.uncc.assignment11.databinding.FragmentToDoListsBinding;
import edu.uncc.assignment11.databinding.ListItemTodoListBinding;
import edu.uncc.assignment11.models.ToDoList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ToDoListsFragment extends Fragment {
    public ToDoListsFragment() {
        // Required empty public constructor
    }

    FragmentToDoListsBinding binding;
    ArrayList<ToDoList> mToDoLists = new ArrayList<>();
    ToDoListAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentToDoListsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("ToDo Lists");

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
        getAllToDoListsForUser();
    }

    // Java
    private void getAllToDoListsForUser() {
        final Activity activity = getActivity();
        if (activity == null) return;

        String token = activity.getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                .getString("token", null);
        if (token == null || token.isEmpty()) {
            activity.runOnUiThread(() ->
                    Toast.makeText(getContext(), "No token found", Toast.LENGTH_SHORT).show());
            return;
        }

        String url = "https://www.theappsdr.com/api/todolists";
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "BEARER " + token)
                .build();

        ((MainActivity) activity).getHttpClient().newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                Activity act = getActivity();
                if (act != null) {
                    act.runOnUiThread(() ->
                            Toast.makeText(getContext(), "Failed to load ToDo Lists", Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                Activity act = getActivity();
                if (act == null) return;

                okhttp3.ResponseBody responseBodyObj = response.body();
                if (responseBodyObj == null) {
                    act.runOnUiThread(() ->
                            Toast.makeText(getContext(), "Empty response", Toast.LENGTH_SHORT).show());
                    return;
                }
                String responseBody = responseBodyObj.string();
                try {
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    if ("ok".equals(jsonResponse.optString("status"))) {
                        JSONArray listArray = jsonResponse.getJSONArray("todolists");
                        mToDoLists.clear();
                        for (int i = 0; i < listArray.length(); i++) {
                            JSONObject listObj = listArray.getJSONObject(i);
                            int id = listObj.getInt("todolist_id");
                            String name = listObj.getString("name");
                            ToDoList list = new ToDoList(id, name);
                            mToDoLists.add(list);
                        }
                        act.runOnUiThread(() -> adapter.notifyDataSetChanged());
                    } else {
                        final String error = jsonResponse.optString("message", "Failed to load lists");
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

    private void deleteToDoList(ToDoList toDoList) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete ToDo List")
                .setMessage("Are you sure you want to delete this ToDo List?")
                .setPositiveButton("OK", (dialog, which) -> {
                    String token = getActivity().getSharedPreferences(
                                    getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                            .getString("token", null);
                    if (token == null || token.isEmpty()) {
                        Toast.makeText(getContext(), "No token found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String url = "https://www.theappsdr.com/api/todolists/delete";
                    RequestBody formBody = new FormBody.Builder()
                            .add("todolist_id", String.valueOf(toDoList.getTodolistId()))
                            .build();
                    Request request = new Request.Builder()
                            .url(url)
                            .post(formBody)
                            .header("Authorization", "BEARER " + token)
                            .build();

                    ((MainActivity) getActivity()).getHttpClient().newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            getActivity().runOnUiThread(() ->
                                    Toast.makeText(getContext(), "Failed to delete list", Toast.LENGTH_SHORT).show());
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            String responseBody = response.body().string();
                            try {
                                JSONObject jsonResponse = new JSONObject(responseBody);
                                if ("ok".equals(jsonResponse.optString("status"))) {
                                    getActivity().runOnUiThread(() -> {
                                        Toast.makeText(getContext(), "ToDo List deleted", Toast.LENGTH_SHORT).show();
                                        getAllToDoListsForUser();
                                    });
                                } else {
                                    final String error = jsonResponse.optString("message", "Delete failed");
                                    getActivity().runOnUiThread(() ->
                                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show());
                                }
                            } catch (JSONException e) {
                                getActivity().runOnUiThread(() ->
                                        Toast.makeText(getContext(), "Error parsing deletion response", Toast.LENGTH_SHORT).show());
                            }
                        }
                    });
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

    ToDoListsListener mListener;

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

    public interface ToDoListsListener {
        void gotoCreateNewToDoList();
        void gotoToDoListDetails(ToDoList toDoList);
        void logout();
    }
}