package com.example.assignment01b;

import android.os.Bundle;
import android.util.Log;
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

public class MainActivity extends AppCompatActivity {

    EditText editTextEnteredTemperature;
    TextView textViewConvertedTemp;
    RadioGroup radioGroupTempConversion;

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
                textViewConvertedTemp.setText(" N/A");
                radioGroupTempConversion.check(R.id.radioButtonCtoF);
            }
        });

        findViewById(R.id.buttonCalculate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioGroupTempConversion = findViewById(R.id.radioGroupTempConversion);
                String enteredTemp = editTextEnteredTemperature.getText().toString();

                try {
                    if (radioGroupTempConversion.getCheckedRadioButtonId() == R.id.radioButtonCtoF) {
                        double temperature = Double.valueOf(enteredTemp);
                        double tempConvertedToF = temperature * 9/5 + 32; // (C×9/5)+32
                        textViewConvertedTemp.setText(String.valueOf(tempConvertedToF));
                    }
                    if (radioGroupTempConversion.getCheckedRadioButtonId() == R.id.radioButtonFtoC) {
                        double temperature = Double.valueOf(enteredTemp);
                        double tempConvertedToC = (temperature - 32) * 5/9; // (F−32)×5/9
                        textViewConvertedTemp.setText(String.valueOf(tempConvertedToC));
                    }
                } catch (NumberFormatException exception) {
                    Toast.makeText(MainActivity.this, "Please enter a valid temperature.", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        findViewById(R.id.buttonCtoF).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // get the entered temperature
//                String enteredTemp = editTextEnteredTemperature.getText().toString();
//                try {
//                    double temperature = Double.valueOf(enteredTemp);
//                    double tempConvertedToF = temperature * 9/5 + 32; // (C×9/5)+32
//                    textViewConvertedTemp.setText(String.valueOf(tempConvertedToF));
//                } catch (NumberFormatException exception) {
//                    Toast.makeText(MainActivity.this, "Please enter a valid temperature.", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//
//        findViewById(R.id.buttonFtoC).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String enteredTemp = editTextEnteredTemperature.getText().toString();
//                try {
//                    double temperature = Double.valueOf(enteredTemp);
//                    double tempConvertedToC = (temperature - 32) * 5/9; // (F−32)×5/9
//                    textViewConvertedTemp.setText(String.valueOf(tempConvertedToC));
//                } catch (NumberFormatException exception) {
//                    Toast.makeText(MainActivity.this, "Please enter a valid temperature.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }
}