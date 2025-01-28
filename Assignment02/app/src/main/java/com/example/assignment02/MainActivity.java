package com.example.assignment02;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    TextView textViewColorHEX, textViewColorRGB;
    TextView textViewRedValue, textViewGreenValue, textViewBlueValue;
    SeekBar seekBarRed, seekBarGreen, seekBarBlue;
    View viewColor;

    Color selectedColor;

    private void updateViewColor() {
        int red = seekBarRed.getProgress();
        int green = seekBarGreen.getProgress();
        int blue = seekBarBlue.getProgress();

        Color color = new Color(red, green, blue);
        int colorValue = android.graphics.Color.rgb(red, green, blue);

        viewColor.setBackgroundColor(colorValue);
        textViewColorHEX.setText("Color HEX: " + color.getHexValue());
        textViewColorRGB.setText("Color RGB: " + color.getRgbValue());
    }

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

        textViewColorHEX = findViewById(R.id.textViewColorHEX);
        textViewColorRGB = findViewById(R.id.textViewColorRBG);
        textViewRedValue = findViewById(R.id.textViewRedValue);
        textViewGreenValue = findViewById(R.id.textViewGreenValue);
        textViewBlueValue = findViewById(R.id.textViewBlueValue);
        seekBarRed = findViewById(R.id.seekBarRed);
        seekBarGreen = findViewById(R.id.seekBarGreen);
        seekBarBlue = findViewById(R.id.seekBarBlue);
        viewColor = findViewById(R.id.viewColor);

        seekBarRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewRedValue.setText(String.valueOf(progress));
                updateViewColor();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarGreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewGreenValue.setText(String.valueOf(progress));
                updateViewColor();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewBlueValue.setText(String.valueOf(progress));
                updateViewColor();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // buttonWhite
        findViewById(R.id.buttonWhite).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Color white = new Color(255, 255, 255);
               viewColor.setBackgroundColor(getColor(R.color.white));

               seekBarRed.setProgress(white.getRedValue());
               seekBarGreen.setProgress(white.getGreenValue());
               seekBarBlue.setProgress(white.getBlueValue());

               textViewRedValue.setText(String.valueOf(seekBarRed.getProgress()));
               textViewGreenValue.setText(String.valueOf(seekBarGreen.getProgress()));
               textViewBlueValue.setText(String.valueOf(seekBarBlue.getProgress()));

               textViewColorHEX.setText("Color HEX: " + white.getHexValue());
               textViewColorRGB.setText("Color RGB: " + white.getRgbValue());

           }

        });

        // buttonBlack
        findViewById(R.id.buttonBlack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Color black = new Color(0, 0, 0);
                viewColor.setBackgroundColor(getColor(R.color.black));

                seekBarRed.setProgress(black.getRedValue());
                seekBarGreen.setProgress(black.getGreenValue());
                seekBarBlue.setProgress(black.getBlueValue());

                textViewRedValue.setText(String.valueOf(seekBarRed.getProgress()));
                textViewGreenValue.setText(String.valueOf(seekBarGreen.getProgress()));
                textViewBlueValue.setText(String.valueOf(seekBarBlue.getProgress()));

                textViewColorHEX.setText("Color HEX: " + black.getHexValue());
                textViewColorRGB.setText("Color RGB: " + black.getRgbValue());
            }

        });

        // buttonBlue
        findViewById(R.id.buttonBlue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Color blue = new Color(6, 71, 235);
                viewColor.setBackgroundColor(getColor(R.color.blue));

                seekBarRed.setProgress(blue.getRedValue());
                seekBarGreen.setProgress(blue.getGreenValue());
                seekBarBlue.setProgress(blue.getBlueValue());

                textViewRedValue.setText(String.valueOf(seekBarRed.getProgress()));
                textViewGreenValue.setText(String.valueOf(seekBarGreen.getProgress()));
                textViewBlueValue.setText(String.valueOf(seekBarBlue.getProgress()));

                textViewColorHEX.setText("Color HEX: " + blue.getHexValue());
                textViewColorRGB.setText("Color RGB: " + blue.getRgbValue());
            }

        });

        // buttonReset
        findViewById(R.id.buttonReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Color defaultGreen = new Color(64, 128, 0);
                viewColor.setBackgroundColor(getColor(R.color.default_color));

                seekBarRed.setProgress(defaultGreen.getRedValue());
                seekBarGreen.setProgress(defaultGreen.getGreenValue());
                seekBarBlue.setProgress(defaultGreen.getBlueValue());

                textViewRedValue.setText(String.valueOf(seekBarRed.getProgress()));
                textViewGreenValue.setText(String.valueOf(seekBarGreen.getProgress()));
                textViewBlueValue.setText(String.valueOf(seekBarBlue.getProgress()));

                textViewColorHEX.setText("Color HEX: " + defaultGreen.getHexValue());
                textViewColorRGB.setText("Color RGB: " + defaultGreen.getRgbValue());
            }

        });
    }
}
