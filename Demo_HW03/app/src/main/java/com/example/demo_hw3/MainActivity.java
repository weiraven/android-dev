package com.example.demo_hw3;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BacFragment.BacFragmentListener, SetProfileFragment.SetProfileFragmentListener {

    ArrayList<Drink> drinks = new ArrayList<>();

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
                .add(R.id.main, new BacFragment(), "bac-fragment")
                .commit();
    }


    @Override
    public void gotoSetProfile() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new SetProfileFragment(), "profile-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void clearAllDrinks() {
        drinks.clear();
    }

    @Override
    public ArrayList<Drink> getAllDrinks() {
        return this.drinks;
    }

    @Override
    public void cancelProfile() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void sendProfile(Profile profile) {
        drinks.clear();
        //find bac fragment
        BacFragment bacFragment = (BacFragment) getSupportFragmentManager().findFragmentByTag("bac-fragment");
        //send profile to bac fragment
        if(bacFragment != null){
            bacFragment.setProfile(profile);
        }
        //pop backstack
        getSupportFragmentManager().popBackStack();
    }
}