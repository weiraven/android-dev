package edu.uncc.assignment08;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import edu.uncc.assignment08.databinding.FragmentTasksBinding;
import edu.uncc.assignment08.databinding.TaskRowItemBinding;
import edu.uncc.assignment08.models.SortSelection;
import edu.uncc.assignment08.models.Task;

public class TasksFragment extends Fragment {
    public TasksFragment() {
        // Required empty public constructor
    }

    FragmentTasksBinding binding;
    SortSelection sortSelection;
    ArrayList<Task> mTasks = new ArrayList<>();
    TasksAdapter adapter;


    public void setSortSelection(SortSelection sortSelection) {
        this.sortSelection = sortSelection;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTasksBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Tasks");
        mTasks = mListener.getTasks();
        adapter = new TasksAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        binding.buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.clearAllTasks();
                adapter.notifyDataSetChanged();
                binding.textViewTasksCount.setText("You have " + adapter.getItemCount() + " tasks.");
            }
        });

        binding.buttonNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoCreateTask();
            }
        });

        binding.buttonSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoSelectSort();
            }
        });

        binding.textViewTasksCount.setText("You have " + adapter.getItemCount() + " tasks.");
    }

    public void applySortSelection(SortSelection sortSelection){
        if (sortSelection != null) {
            switch (sortSelection.getSortAttribute()) {
                case "date":
                    if (sortSelection.getSortOrder().equals("ASC")) {
                        Collections.sort(mTasks, Comparator.comparing(Task::getDate));
                    } else {
                        Collections.sort(mTasks, (task1, task2) -> task2.getDate().compareTo(task1.getDate()));
                    }
                    break;
                case "name":
                    if (sortSelection.getSortOrder().equals("ASC")) {
                        Collections.sort(mTasks, Comparator.comparing(Task::getName));
                    } else {
                        Collections.sort(mTasks, (task1, task2) -> task2.getName().compareTo(task1.getName()));
                    }
                    break;
                case "priority":
                    if (sortSelection.getSortOrder().equals("ASC")) {
                        Collections.sort(mTasks, Comparator.comparingInt(Task::getPriority));
                    } else {
                        Collections.sort(mTasks, (task1, task2) -> task2.getPriority() - task1.getPriority());
                    }
                    break;
            }
            adapter.notifyDataSetChanged();
            binding.textViewTasksCount.setText("You have " + adapter.getItemCount() + " tasks.");
        }
    }

    class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {

        @NonNull
        @Override
        public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TaskRowItemBinding itemBinding = TaskRowItemBinding.inflate(getLayoutInflater(), parent, false);
            return new TaskViewHolder(itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
            Task task = mTasks.get(position);
            holder.bind(task);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.gotoTaskDetails(task);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mTasks.size();
        }

        class TaskViewHolder extends RecyclerView.ViewHolder {
            TaskRowItemBinding itemBinding;
            Task mTask;
            public TaskViewHolder(TaskRowItemBinding itemBinding) {
                super(itemBinding.getRoot());
                this.itemBinding = itemBinding;
            }

            public void bind(Task task) {
                this.mTask = task;
                itemBinding.textViewTaskName.setText(task.getName());
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                itemBinding.textViewTaskDate.setText(sdf.format(task.getDate()));

                String taskPriority;
                if (task.getPriority() == 2){
                    taskPriority = "Medium";
                } else if (task.getPriority() == 3) {
                    taskPriority = "High";
                } else {
                    taskPriority = "Low";
                }
                itemBinding.textViewTaskPriority.setText(taskPriority);

                itemBinding.imageViewTrashIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTasks.remove(mTask);
                        notifyDataSetChanged();
                        binding.textViewTasksCount.setText("You have " + adapter.getItemCount() + " tasks.");
                    }
                });


            }


        }

    }
    TasksListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof TasksListener) {
            mListener = (TasksListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement TasksListener");
        }
    }

    interface TasksListener{
        void gotoCreateTask();
        void gotoSelectSort();
        void clearAllTasks();
        void gotoTaskDetails(Task task);
        ArrayList<Task> getTasks();
    }
}