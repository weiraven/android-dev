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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import edu.uncc.assignment06.databinding.FragmentTasksBinding;

public class TasksFragment extends Fragment {

    private ArrayList<Task> taskList;
    private int currentTaskIndex = 0;

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
        sortTaskListByDateDescending();
        updateTaskCount();
        updateCardViewVisibility();
        if (taskList != null && !taskList.isEmpty()) {
            displayTask(currentTaskIndex);
        }

        binding.buttonCreateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToCreateTask();
            }
        });

        // Click listener for "Previous" icon
        binding.cardViewTask.findViewById(R.id.imageViewPrevious).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentTaskIndex > 0) {
                    currentTaskIndex--;
                    displayTask(currentTaskIndex);
                }
            }
        });

        // Click listener for "Next" icon
        binding.cardViewTask.findViewById(R.id.imageViewNext).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (currentTaskIndex < taskList.size() - 1){
                   currentTaskIndex++;
                   displayTask(currentTaskIndex);
               }
           }
        });

        binding.cardViewTask.findViewById(R.id.imageViewDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(taskList != null && !taskList.isEmpty()) {
                    mListener.deleteTask(taskList.get(currentTaskIndex));
                }
                taskList = mListener.getTaskList();
                sortTaskListByDateDescending();
                updateTaskCount();
                updateCardViewVisibility();
                if (taskList.isEmpty()) {
                    clearTaskDetails();
                } else {
                    if (currentTaskIndex >= taskList.size()) {
                        currentTaskIndex = taskList.size() - 1;
                    }
                    displayTask(currentTaskIndex);
                }
            }
        });
    }

    private void displayTask(int index) {
        Task task = taskList.get(index);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        ((TextView) binding.cardViewTask.findViewById(R.id.textViewTaskName)).setText(task.getName());
        ((TextView) binding.cardViewTask.findViewById(R.id.textViewTaskDate)).setText(sdf.format(task.getDate()));
        ((TextView) binding.cardViewTask.findViewById(R.id.textViewTaskPriority)).setText(task.getPriority());
        ((TextView) binding.cardViewTask.findViewById(R.id.textViewTaskOutOf)).setText("Task " + (index + 1) + " of " + taskList.size());
    }

    private void clearTaskDetails() {
        ((TextView) binding.cardViewTask.findViewById(R.id.textViewTaskName)).setText("");
        ((TextView) binding.cardViewTask.findViewById(R.id.textViewTaskDate)).setText("");
        ((TextView) binding.cardViewTask.findViewById(R.id.textViewTaskPriority)).setText("");
        ((TextView) binding.cardViewTask.findViewById(R.id.textViewTaskOutOf)).setText("");
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

    private void updateCardViewVisibility() {
        if (taskList == null || taskList.isEmpty()) {
            binding.cardViewTask.setVisibility(View.GONE);
        } else {
            binding.cardViewTask.setVisibility(View.VISIBLE);
        }
    }

    private void sortTaskListByDateDescending() {
        if (taskList != null) {
            Collections.sort(taskList, new Comparator<Task>() {
                @Override
                public int compare(Task t1, Task t2) {
                    return t1.getDate().compareTo(t2.getDate());
                }
            });
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