package com.example.assignment04;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CreateUserActivity extends AppCompatActivity {

    EditText editTextName, editTextEmail;
    RadioGroup radioGroupRole;
    public static final String KEY_PROFILE = "profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_user);
        setTitle("CreateUser");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmailAddress);
        radioGroupRole = findViewById(R.id.radioGroupRole);

        findViewById(R.id.buttonNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();

                int selectedRoleId = radioGroupRole.getCheckedRadioButtonId();
                String role = getString(R.string.student_radio_label);
                if (selectedRoleId == R.id.radioButtonEmployee) {
                    role = getString(R.string.employee_radio_label);
                } else if (selectedRoleId == R.id.radioButtonOther){
                    role = getString(R.string.other_radio_label);
                }

                if(name.isEmpty()){
                    Toast.makeText(CreateUserActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
                } else if(email.isEmpty()){
                    Toast.makeText(CreateUserActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                } else {
                    Profile profile = new Profile(name, email, role);
                    Intent intent = new Intent(CreateUserActivity.this, ProfileActivity.class);
                    intent.putExtra(KEY_PROFILE, profile);
                    startActivity(intent);
                }
            }
        });

    }
}