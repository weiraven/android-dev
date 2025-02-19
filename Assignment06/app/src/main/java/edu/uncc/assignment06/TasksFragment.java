package edu.uncc.assignment06;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import edu.uncc.assignment06.databinding.FragmentTasksBinding;

public class TasksFragment extends Fragment {

    private ArrayList<Task> taskList;

    public TasksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentTasksBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTasksBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Tasks");
        taskList = mListener.getTaskList();
        updateTaskCount();
        int taskCounter = 1;

        for (Task task : taskList) {
            binding.textViewTaskName.setText(task.getName());
            binding.textViewTaskDate.setText(task.getDate().toString());
            binding.textViewTaskPriority.setText(task.getPriority());
            binding.textViewTaskOutOf.setText("Task " + taskCounter + " of " + taskList.size());
            taskCounter++;
        }

        binding.buttonCreateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToCreateTask();
            }
        });
    }

    private void updateTaskCount() {
        if (taskList == null || taskList.isEmpty()) {
            binding.textViewTasksCount.setText("You have 0 tasks");
        } else if (taskList.size() == 1) {
            binding.textViewTasksCount.setText("You have 1 task");
        } else {
            binding.textViewTasksCount.setText("You have " + taskList.size() + " tasks");
        }
    }

    TasksFragmentListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (TasksFragmentListener) context;
    }

    interface TasksFragmentListener{
        void deleteTask(Task task);
        void goToCreateTask();
        ArrayList<Task> getTaskList();
    }

}