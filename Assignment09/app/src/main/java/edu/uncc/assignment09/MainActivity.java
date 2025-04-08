package edu.uncc.assignment09;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.uncc.assignment09.fragments.AddUserFragment;
import edu.uncc.assignment09.fragments.FilterFragment;
import edu.uncc.assignment09.fragments.SelectStateFragment;
import edu.uncc.assignment09.fragments.SortFragment;
import edu.uncc.assignment09.fragments.UsersFragment;
import edu.uncc.assignment09.models.AppDatabase;
import edu.uncc.assignment09.models.CreditCategory;
import edu.uncc.assignment09.models.State;
import edu.uncc.assignment09.models.User;

public class MainActivity extends AppCompatActivity implements UsersFragment.UsersListener, AddUserFragment.AddUserListener,
        SelectStateFragment.SelectStateListener, SortFragment.SortListener, FilterFragment.FilterListener {
    ArrayList<User> mUsers = new ArrayList<>();
    AppDatabase db;

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

        db = Room.databaseBuilder(this, AppDatabase.class, "users-db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new UsersFragment(), "users-fragment")
                .commit();
    }

    @Override
    public void gotoAddUser() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new AddUserFragment(), "add-user-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoSelectFilter() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new FilterFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoSelectSort() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new SortFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public ArrayList<User> getAllUsers() {
        mUsers.clear();
        mUsers.addAll(db.userDao().getAll());
        return mUsers;
    }

    @Override
    public void deleteUser(User user) {
        db.userDao().delete(user);
    }

    @Override
    public List<User> getFilteredAndSortedUsers(int threshold, String sortBy,String sortDirection) {
        List<User> users = new ArrayList<>();
        if(sortBy.equals("name")){
            users = db.userDao().getFilteredScoresSortByName(threshold);
        } else if(sortBy.equals("age")){
            users = db.userDao().getFilteredScoresSortByAge(threshold);
        } else if(sortBy.equals("credit_score")){
            users = db.userDao().getFilteredScoresSortByScore(threshold);
        } else if(sortBy.equals("state")){
            users = db.userDao().getFilteredScoresSortByState(threshold);
        }


        if(!sortDirection.equals("ASC")){
            Collections.reverse(users);
        }

        return users;
    }

    @Override
    public void sendBackNewUser(User user) {
        db.userDao().insertAll(user);
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onStateSelected(State state) {
        AddUserFragment addUserFragment = (AddUserFragment) getSupportFragmentManager().findFragmentByTag("add-user-fragment");
        if(addUserFragment != null){
            addUserFragment.setSelectedState(state);
        }
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onSortSelected(String sort) {
        UsersFragment usersFragment = (UsersFragment) getSupportFragmentManager().findFragmentByTag("users-fragment");
        if(usersFragment != null){
            usersFragment.setSelectedSort(sort);
        }
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onFilterSelected(CreditCategory creditCategory) {
        UsersFragment usersFragment = (UsersFragment) getSupportFragmentManager().findFragmentByTag("users-fragment");
        if(usersFragment != null){
            usersFragment.setSelectedFilterCategory(creditCategory);
        }
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void cancelAddOrSelection() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void gotoSelectState() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new SelectStateFragment())
                .addToBackStack(null)
                .commit();
    }
}