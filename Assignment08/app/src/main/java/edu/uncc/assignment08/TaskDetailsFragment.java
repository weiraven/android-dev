package edu.uncc.assignment08;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;

import edu.uncc.assignment08.databinding.FragmentTaskDetailsBinding;
import edu.uncc.assignment08.models.Task;

public class TaskDetailsFragment extends Fragment {
    private static final String ARG_PARAM_TASK = "ARG_PARAM_TASK";
    private Task mTask;

    public TaskDetailsFragment() {
        // Required empty public constructor
    }

    public static TaskDetailsFragment newInstance(Task task) {
        TaskDetailsFragment fragment = new TaskDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_TASK, task);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTask = (Task)getArguments().getSerializable(ARG_PARAM_TASK);
        }
    }

    FragmentTaskDetailsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTaskDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Task Summary");

        if (mTask != null) {
            binding.textViewTaskNameDisplay.setText(mTask.getName());

            String taskPriority;
            if (mTask.getPriority() == 2){
                taskPriority = "Medium";
            } else if (mTask.getPriority() == 3) {
                taskPriority = "High";
            } else {
                taskPriority = "Low";
            }

            binding.textViewPriorityDisplay.setText(taskPriority);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            binding.textViewDateDisplay.setText(sdf.format(mTask.getDate()));
        }

        binding.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.deleteTask(mTask);
            }
        });

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goBack();
            }
        });

    }

    TaskDetailsListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof TaskDetailsListener) {
            mListener = (TaskDetailsListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement TaskDetailsListener");
        }
    }

    interface TaskDetailsListener {
        void deleteTask(Task task);
        void goBack();
    }
}