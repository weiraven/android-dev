package com.example.assignment05;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements WelcomeFragment.WelcomeFragmentListener, CreateUserFragment.CreateUserFragmentListener, ProfileFragment.ProfileFragmentListener, EditUserFragment.EditUserFragmentListener {

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

        getSupportFragmentManager().beginTransaction()
                .add(R.id.main, new WelcomeFragment(), "welcome-fragment")
                .commit();
    }

    @Override
    public void gotoCreateUser() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new CreateUserFragment(), "create-user-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoProfile(Profile profile) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, ProfileFragment.newInstance(profile))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoEditUser(Profile profile) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, EditUserFragment.newInstance(profile))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void sendUpdatedProfile(Profile updatedProfile) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, ProfileFragment.newInstance(updatedProfile))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void cancelEdit() {
        getSupportFragmentManager().popBackStack();
    }

}