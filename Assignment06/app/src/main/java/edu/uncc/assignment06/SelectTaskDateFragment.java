package edu.uncc.assignment06;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import edu.uncc.assignment06.databinding.FragmentSelectTaskDateBinding;

public class SelectTaskDateFragment extends Fragment {


    public SelectTaskDateFragment() {
        // Required empty public constructor
    }

    FragmentSelectTaskDateBinding binding;
    private Date selectedDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSelectTaskDateBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Select Task Date");

        binding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                // Get the selected date as a timestamp
                long selectedDateTimestamp = calendar.getTimeInMillis();
                // Create a Date object
                selectedDate = new Date(selectedDateTimestamp);
                // Update the display with newly selected date
                updateSelectedDate(selectedDate);
            }
        });

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.cancelSelectDate();
            }
        });

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.submitSelectDate(selectedDate);
            }

        });
    }

    private void updateSelectedDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(date);

        binding.textViewDateDisplay.setText("Selected Task Date: " + formattedDate);
    }
    SelectTaskDateFragmentListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SelectTaskDateFragmentListener) {
            mListener = (SelectTaskDateFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement SelectTaskDateFragmentListener");
        }

    }

    interface SelectTaskDateFragmentListener{
        void cancelSelectDate();
        void submitSelectDate(Date date);
    }
}