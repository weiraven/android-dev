package edu.uncc.assignment12;

import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import edu.uncc.assignment12.fragments.todo.AddItemToToDoListFragment;
import edu.uncc.assignment12.fragments.auth.LoginFragment;
import edu.uncc.assignment12.fragments.todo.CreateNewToDoListFragment;
import edu.uncc.assignment12.fragments.auth.SignUpFragment;
import edu.uncc.assignment12.fragments.todo.ToDoListDetailsFragment;
import edu.uncc.assignment12.fragments.todo.ToDoListsFragment;
import edu.uncc.assignment12.models.ToDoList;


public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener, SignUpFragment.SignUpListener,
        ToDoListsFragment.ToDoListsListener, ToDoListDetailsFragment.ToDoListDetailsListener, CreateNewToDoListFragment.CreateNewToDoListListener,
        AddItemToToDoListFragment.AddItemToListListener{

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Show To-Do lists if use is already logged in, otherwise show login
        if (auth.getCurrentUser() != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main, new ToDoListsFragment())
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main, new LoginFragment())
                    .commit();
        }

    }

    @Override
    public void gotoSignUpUser() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new SignUpFragment())
                .commit();
    }

    @Override
    public void onLoginSuccessful() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main, new ToDoListsFragment());
        transaction.commit();
    }

    @Override
    public void gotoLogin() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new LoginFragment())
                .commit();
    }

    @Override
    public void onSignUpSuccess() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new ToDoListsFragment())
                .commit();
    }

    @Override
    public void gotoCreateNewToDoList() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new CreateNewToDoListFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoToDoListDetails(ToDoList toDoList) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, ToDoListDetailsFragment.newInstance(toDoList))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void logout() {
        // Firebase sign-out
        auth.signOut();

        // Show LoginFragment after logout
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new LoginFragment())
                .commit();
    }

    @Override
    public void onSuccessCreateNewToDoList() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onCancelCreateNewToDoList() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void gotoAddListItem(ToDoList toDoList) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, AddItemToToDoListFragment.newInstance(toDoList))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goBackToToDoLists() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onSuccessAddItemToList() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onCancelAddItemToList(ToDoList todoList) {
        getSupportFragmentManager().popBackStack();
    }
}