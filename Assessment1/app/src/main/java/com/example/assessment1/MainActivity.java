package com.example.assessment1;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText editTextEnteredWeight;
    RadioGroup radioGroupGender;
    TextView textViewBeerCount, textViewWineCount, textViewLiquorCount, textViewMaltCount, textViewBACLevel, textViewStatus;
    View viewStatus;
    Profile profile;
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

        editTextEnteredWeight = findViewById(R.id.editTextEnteredWeight);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        textViewBeerCount = findViewById(R.id.textViewBeerCount);
        textViewWineCount = findViewById(R.id.textViewWineCount);
        textViewLiquorCount = findViewById(R.id.textViewLiquorCount);
        textViewMaltCount = findViewById(R.id.textViewMaltCount);
        textViewBACLevel = findViewById(R.id.textViewBACLevel);
        textViewStatus = findViewById(R.id.textViewStatus);
        viewStatus = findViewById(R.id.viewStatus);

        findViewById(R.id.buttonBeerPlus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String drinkCount = textViewBeerCount.getText().toString();
                int count = Integer.parseInt(drinkCount);
                if (count < 10) {
                    count++;
                    textViewBeerCount.setText(String.valueOf(count));
                } else {
                    textViewBeerCount.setText("10");
                }
            }
        });

        findViewById(R.id.buttonBeerMinus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String drinkCount = textViewBeerCount.getText().toString();
                int count = Integer.parseInt(drinkCount);
                if (count > 0) {
                    count--;
                    textViewBeerCount.setText(String.valueOf(count));
                } else {
                    textViewBeerCount.setText("0");
                }
            }
        });

        findViewById(R.id.buttonWinePlus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String drinkCount = textViewWineCount.getText().toString();
                int count = Integer.parseInt(drinkCount);
                if (count < 10) {
                    count++;
                    textViewWineCount.setText(String.valueOf(count));
                } else {
                    textViewWineCount.setText("10");
                }
            }
        });

        findViewById(R.id.buttonWineMinus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String drinkCount = textViewWineCount.getText().toString();
                int count = Integer.parseInt(drinkCount);
                if (count > 0) {
                    count--;
                    textViewWineCount.setText(String.valueOf(count));
                } else {
                    textViewWineCount.setText("0");
                }
            }
        });

        findViewById(R.id.buttonLiquorPlus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String drinkCount = textViewLiquorCount.getText().toString();
                int count = Integer.parseInt(drinkCount);
                if (count < 10) {
                    count++;
                    textViewLiquorCount.setText(String.valueOf(count));
                } else {
                    textViewLiquorCount.setText("10");
                }
            }
        });

        findViewById(R.id.buttonLiquorMinus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String drinkCount = textViewLiquorCount.getText().toString();
                int count = Integer.parseInt(drinkCount);
                if (count > 0) {
                    count--;
                    textViewLiquorCount.setText(String.valueOf(count));
                } else {
                    textViewLiquorCount.setText("0");
                }
            }
        });

        findViewById(R.id.buttonMaltPlus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String drinkCount = textViewMaltCount.getText().toString();
                int count = Integer.parseInt(drinkCount);
                if (count < 10) {
                    count++;
                    textViewMaltCount.setText(String.valueOf(count));
                } else {
                    textViewMaltCount.setText("10");
                }
            }
        });

        findViewById(R.id.buttonMaltMinus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String drinkCount = textViewMaltCount.getText().toString();
                int count = Integer.parseInt(drinkCount);
                if (count > 0) {
                    count--;
                    textViewMaltCount.setText(String.valueOf(count));
                } else {
                    textViewMaltCount.setText("0");
                }
            }
        });

        findViewById(R.id.buttonCalculate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredWeight = editTextEnteredWeight.getText().toString();
                int beerCount = Integer.parseInt(textViewBeerCount.getText().toString());
                int wineCount = Integer.parseInt(textViewWineCount.getText().toString());
                int liquorCount = Integer.parseInt(textViewLiquorCount.getText().toString());
                int maltCount = Integer.parseInt(textViewMaltCount.getText().toString());

                Drink beers = new Drink(beerCount, 5);
                Drink wines = new Drink(wineCount, 12);
                Drink liquors = new Drink(liquorCount, 40);
                Drink malts = new Drink(maltCount, 7);

                drinks.add(beers);
                drinks.add(wines);
                drinks.add(liquors);
                drinks.add(malts);

                try {
                    double weight = Double.valueOf(enteredWeight);
                    if (weight > 0) {
                        String gender = "Female";
                        if(radioGroupGender.getCheckedRadioButtonId() == R.id.radioButtonMale){
                            gender = "Male";
                        }
                        profile = new Profile(gender, weight);

                    } else {
                        Toast.makeText(MainActivity.this, "Enter a valid weight!", Toast.LENGTH_SHORT).show();
                    }

                    calculateBAC();
                    drinks.clear();

                } catch(NumberFormatException e) {

                    Toast.makeText(MainActivity.this, "Enter a valid weight", Toast.LENGTH_SHORT).show();

                }
            }
        });

        findViewById(R.id.buttonReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextEnteredWeight.setText("");
                radioGroupGender.check(R.id.radioButtonFemale);
                textViewBeerCount.setText("0");
                textViewWineCount.setText("0");
                textViewLiquorCount.setText("0");
                textViewMaltCount.setText("0");
                textViewBACLevel.setText("Your BAC Level: 0.00%");
                textViewStatus.setText("You're safe");
                viewStatus.setBackgroundColor(getColor(R.color.safe_color));
            }
        });
    }
    void calculateBAC() {

        if (drinks.size() == 0) {
            textViewBACLevel.setText("Your BAC Level: 0.00%");
            textViewStatus.setText("You're safe");
            viewStatus.setBackgroundColor(getColor(R.color.safe_color));
        } else {

            double valueA = 0.0;
            for (Drink drink : drinks) {
                valueA = valueA + drink.getSize() * drink.getPercentage() / 100.0;
            }
            double valueR = 0.73;
            if (profile.getGender().equals("Female")) {
                valueR = 0.66;
            }
            double bac = valueA * 5.14 / (profile.getWeight() * valueR);
            textViewBACLevel.setText("Your BAC Level: " + String.valueOf(String.format("%.3f", bac)) + "%");

            if(bac <= 0.08) {
                textViewStatus.setText("You're Safe");
                viewStatus.setBackgroundColor(getColor(R.color.safe_color));
            } else if(bac <= 0.2){
                textViewStatus.setText("Be careful.");
                viewStatus.setBackgroundColor(getColor(R.color.be_careful_color));
            } else {
                textViewStatus.setText("Over the limit!");
                viewStatus.setBackgroundColor(getColor(R.color.over_limit_color));
            }
        }
    }
}
