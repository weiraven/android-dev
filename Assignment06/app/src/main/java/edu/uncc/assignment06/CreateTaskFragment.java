package edu.uncc.assignment06;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.uncc.assignment06.databinding.FragmentCreateTaskBinding;


public class CreateTaskFragment extends Fragment {

    public CreateTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    FragmentCreateTaskBinding binding;
    Date mDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateTaskBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Create Task");

        binding.buttonSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToSelectTaskDate();
            }
        });

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.cancelCreateTask();
            }
        });

        if(mDate != null){
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            binding.textViewDate.setText(sdf.format(mDate));
        } else {
            binding.textViewDate.setText("N/A");
        }

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.editTextTaskName.getText().toString();
                if (name.isEmpty()) {
                    Toast.makeText(getActivity(), "Task name is required.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mDate == null) {
                    Toast.makeText(getActivity(), "Please select a valid date.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String priority = "High";
                if(binding.radioGroup.getCheckedRadioButtonId() == R.id.radioButtonMedium){
                    priority = "Medium";
                } else if(binding.radioGroup.getCheckedRadioButtonId() == R.id.radioButtonLow){
                    priority = "Low";
                }

                Task newTask = new Task(name, mDate, priority);
                mListener.addTaskToList(newTask);
            }
        });
    }

    CreateTaskFragmentListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CreateTaskFragmentListener) {
            mListener = (CreateTaskFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement CreateTaskFragmentListener");
        }
    }

    interface CreateTaskFragmentListener {
        void addTaskToList(Task task);
        void cancelCreateTask();
        void goToSelectTaskDate();
    }
}