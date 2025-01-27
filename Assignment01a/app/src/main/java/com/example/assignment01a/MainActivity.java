package com.example.assignment01a;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    EditText editTextEnteredTemperature;
    TextView textViewConvertedTemp;
    public static final String TAG = "demo";

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

        editTextEnteredTemperature = findViewById(R.id.editTextEnteredTemperature);
        textViewConvertedTemp = findViewById(R.id.textViewConvertedTemp);

        findViewById(R.id.buttonReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextEnteredTemperature.setText("");
                textViewConvertedTemp.setText("");
            }
        });

        findViewById(R.id.buttonCtoF).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get the entered temperature
                String enteredTemp = editTextEnteredTemperature.getText().toString();
                try {
                    double temperature = Double.valueOf(enteredTemp);
                    Log.d(TAG, "onClick: ");
                    double tempConvertedToF = temperature * 9/5 + 32; // (C×9/5)+32

                    textViewConvertedTemp.setText(String.valueOf(tempConvertedToF));

                } catch (NumberFormatException exception) {
                    Toast.makeText(MainActivity.this, "Please enter a valid temperature.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        findViewById(R.id.buttonFtoC).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String enteredTemp = editTextEnteredTemperature.getText().toString();
                try {
                    double temperature = Double.valueOf(enteredTemp);
                    Log.d(TAG, "onClick: ");
                    double tempConvertedToC = (temperature - 32) * 5/9; // (F−32)×5/9

                    textViewConvertedTemp.setText(String.valueOf(tempConvertedToC));

                } catch (NumberFormatException exception) {
                    Toast.makeText(MainActivity.this, "Please enter a valid temperature.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}