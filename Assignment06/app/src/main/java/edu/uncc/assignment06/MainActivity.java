package edu.uncc.assignment06;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements TasksFragment.TasksFragmentListener, CreateTaskFragment.CreateTaskFragmentListener, SelectTaskDateFragment.SelectTaskDateFragmentListener {

    ArrayList<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tasks = new ArrayList<>();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.main, new TasksFragment(), "tasks-fragment")
                .commit();
    }

    @Override
    public void deleteTask(Task task) {
        tasks.remove(task);
    }

    @Override
    public void goToCreateTask() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new CreateTaskFragment(), "create-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public ArrayList<Task> getTaskList() {
        return this.tasks;
    }

    @Override
    public void addTaskToList(Task task) {
        tasks.add(task);
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void cancelCreateTask() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void goToSelectTaskDate() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new SelectTaskDateFragment(), "select-date-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void cancelSelectDate() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void submitSelectDate(Date date) {
        CreateTaskFragment createTaskFragment = (CreateTaskFragment) getSupportFragmentManager().findFragmentByTag("create-fragment");
        if(createTaskFragment != null){
            createTaskFragment.mDate = date;
        }
        getSupportFragmentManager().popBackStack();
    }
}