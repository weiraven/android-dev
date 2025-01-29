package com.example.assignment03;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    EditText editTextEnteredListPrice;
    RadioGroup radioGroupSalePercent;
    SeekBar seekBarCustomPercent;
    TextView textViewCustomSliderValue, textViewDiscount, textViewFinalPrice;

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

        editTextEnteredListPrice = findViewById(R.id.editTextEnteredListPrice);
        radioGroupSalePercent = findViewById(R.id.radioGroupSalePercent);
        seekBarCustomPercent = findViewById(R.id.seekBarCustomPercent);
        //radioButton10, radioButton15, radioButton18, radioButtonCustom
        textViewCustomSliderValue = findViewById(R.id.textViewCustomSliderValue);
        textViewDiscount = findViewById(R.id.textViewDiscount);
        textViewFinalPrice = findViewById(R.id.textViewFinalPrice);

        seekBarCustomPercent.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewCustomSliderValue.setText(String.format("%d%%", progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // buttonReset
        findViewById(R.id.buttonReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextEnteredListPrice.setText(R.string.item_price_hint);
                radioGroupSalePercent.check(R.id.radioButton10);

                seekBarCustomPercent.setProgress(25);
                textViewCustomSliderValue.setText(R.string.seekbar_default_percent);

                textViewDiscount.setText(R.string.discount_default);
                textViewFinalPrice.setText(R.string.final_price_default);
            }

        });

        // buttonCalculate
        findViewById(R.id.buttonCalculate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredListPrice = editTextEnteredListPrice.getText().toString();
                int customSalePercent = seekBarCustomPercent.getProgress(); // This should be an integer value representing the percent
                try {
                    double price = Double.valueOf(enteredListPrice);
                    double discountAmount;
                    double finalPrice;
                    double discountPercent = 0.0;

                    int checkedId = radioGroupSalePercent.getCheckedRadioButtonId();
                    
                    if (checkedId == R.id.radioButton10) {
                        discountPercent = 0.1;
                    } else if (checkedId == R.id.radioButton15) {
                        discountPercent = 0.15;
                    } else if (checkedId == R.id.radioButton18) {
                        discountPercent = 0.18;
                    } else if (checkedId == R.id.radioButtonCustom) {
                        discountPercent = customSalePercent / 100.0;
                    } else {
                        Toast.makeText(MainActivity.this, "Select a discount percentage.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    discountAmount = price * discountPercent;
                    finalPrice = price - discountAmount;

                    textViewDiscount.setText(String.format("Discount: %.2f", discountAmount));
                    textViewFinalPrice.setText(String.format("Final Price: %.2f", finalPrice));

                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Enter a valid list price.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}

