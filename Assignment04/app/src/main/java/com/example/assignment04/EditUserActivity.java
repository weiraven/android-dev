package com.example.assignment04;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditUserActivity extends AppCompatActivity {

    public static final String KEY_PROFILE = "profile";

    EditText editTextEditName, editTextEditEmail;
    RadioGroup radioGroupEditRole;
    RadioButton radioButtonStudentEdit, radioButtonEmployeeEdit, radioButtonOtherEdit;
    Button buttonEditCancel, buttonEditSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_user);
        setTitle("EditUser");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.edit_user), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        editTextEditName = findViewById(R.id.editTextEditName);
        editTextEditEmail = findViewById(R.id.editTextEditEmail);
        radioGroupEditRole = findViewById(R.id.radioGroupEditRole);
        radioButtonStudentEdit = findViewById(R.id.radioButtonStudentEdit);
        radioButtonEmployeeEdit = findViewById(R.id.radioButtonEmployeeEdit);
        radioButtonOtherEdit = findViewById(R.id.radioButtonOtherEdit);
        buttonEditCancel = findViewById(R.id.buttonEditCancel);
        buttonEditSubmit = findViewById(R.id.buttonEditSubmit);


        Profile profile = (Profile) getIntent().getSerializableExtra(KEY_PROFILE);
        
        if (profile != null) {
            editTextEditName.setText(profile.getName());
            editTextEditEmail.setText(profile.getEmail());

            if (profile.getRole().equals(getString(R.string.student_radio_label))) {
                radioButtonStudentEdit.setChecked(true);
            } else if (profile.getRole().equals(getString(R.string.employee_radio_label))) {
                radioButtonEmployeeEdit.setChecked(true);
            } else if (profile.getRole().equals(getString(R.string.other_radio_label))) {
                radioButtonOtherEdit.setChecked(true);
            }

        }

        buttonEditCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonEditSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedName = editTextEditName.getText().toString();
                String updatedEmail = editTextEditEmail.getText().toString();
                String updatedRole = "";

                int selectedId = radioGroupEditRole.getCheckedRadioButtonId();
                if (selectedId == R.id.radioButtonStudentEdit) {
                    updatedRole = "Student";
                } else if (selectedId == R.id.radioButtonEmployeeEdit) {
                    updatedRole = "Employee";
                } else if (selectedId == R.id.radioButtonOtherEdit) {
                    updatedRole = "Other";
                }

                if(updatedName.isEmpty()){
                    Toast.makeText(EditUserActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
                } else if(updatedEmail.isEmpty()){
                    Toast.makeText(EditUserActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                } else {
                    Profile updatedProfile = new Profile(updatedName, updatedEmail, updatedRole);
                    Intent intent = new Intent();
                    intent.putExtra(KEY_PROFILE, updatedProfile);
                    setResult(RESULT_OK, intent);
                    finish();
                }

            }
        });


    }
}