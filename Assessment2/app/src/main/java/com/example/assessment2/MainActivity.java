package com.example.assessment2;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.assessment2.SelectWeightFragment.SelectWeightListener;

public class MainActivity extends AppCompatActivity implements CalorieCalculatorFragment.CalorieCalculatorListener, SelectGenderFragment.SelectGenderListener, SelectWeightListener, SelectHeightFragment.SelectHeightListener, SelectAgeFragment.SelectAgeListener, SelectActivityLevelFragment.SelectActivityLevelListener {

    String gender;
    double weight;
    int heightFeet;
    int heightInches;
    int age;
    String activityLevel;

    Calorie details;

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
                .add(R.id.main, new CalorieCalculatorFragment(), "calorie-calculator-fragment")
                .commit();
    }

    @Override
    public void goToSelectGender() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new SelectGenderFragment(), "gender-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToSelectWeight() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new SelectWeightFragment(), "weight-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToSelectHeight() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new SelectHeightFragment(), "height-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToSelectAge() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new SelectAgeFragment(), "age-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToSelectActivityLevel() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new SelectActivityLevelFragment(), "activity-level-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void submitDetails(Calorie details) {
        CalorieCalculatorFragment fragment = (CalorieCalculatorFragment) getSupportFragmentManager().findFragmentByTag("calorie-calculator-fragment");
        if (fragment != null) {
            fragment.setDetails(details);
        }
    }


    @Override
    public void cancelSelectGender() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void submitGender(String gender) {
        CalorieCalculatorFragment fragment = (CalorieCalculatorFragment) getSupportFragmentManager().findFragmentByTag("calorie-calculator-fragment");
        if (fragment != null) {
            fragment.setGender(gender);
        }
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void cancelSelectWeight() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void submitWeight(double weight) {
        CalorieCalculatorFragment fragment = (CalorieCalculatorFragment) getSupportFragmentManager().findFragmentByTag("calorie-calculator-fragment");
        if (fragment != null) {
            fragment.setWeight(weight);
        }
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void cancelSelectHeight() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void submitHeight(int heightFeet, int heightInches) {
        CalorieCalculatorFragment fragment = (CalorieCalculatorFragment) getSupportFragmentManager().findFragmentByTag("calorie-calculator-fragment");
        if (fragment != null) {
            fragment.setHeight(heightFeet, heightInches);
        }
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void cancelSelectAge() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void submitAge(int age) {
        CalorieCalculatorFragment fragment = (CalorieCalculatorFragment) getSupportFragmentManager().findFragmentByTag("calorie-calculator-fragment");
        if (fragment != null) {
            fragment.setAge(age);
        }
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void cancelSelectActivityLevel() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void submitActivityLevel(String activityLevel) {
        CalorieCalculatorFragment fragment = (CalorieCalculatorFragment) getSupportFragmentManager().findFragmentByTag("calorie-calculator-fragment");
        if (fragment != null) {
            fragment.setActivityLevel(activityLevel);
        }
        getSupportFragmentManager().popBackStack();
    }
}