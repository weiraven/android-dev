package com.example.assignment04;

import static com.example.assignment04.CreateUserActivity.KEY_PROFILE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProfileActivity extends AppCompatActivity {

    TextView textViewName, textViewEmail, textViewRole;
    Button buttonUpdate;
    Profile currentProfile;
    public static final String KEY_PROFILE = "profile";

    ActivityResultLauncher<Intent> startEditActivityForResult =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK && result.getData() != null && result.getData().hasExtra(KEY_PROFILE)){
                        Intent data = result.getData();
                        if(data != null && data.hasExtra(KEY_PROFILE)){
                            currentProfile = (Profile) data.getSerializableExtra(KEY_PROFILE);
                            textViewName.setText(currentProfile.getName());
                            textViewEmail.setText(currentProfile.getEmail());
                            textViewRole.setText(currentProfile.getRole());
                        }
                    }
                }
            });
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        setTitle("Profile");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textViewName = findViewById(R.id.textViewUserName);
        textViewEmail = findViewById(R.id.textViewUserEmail);
        textViewRole = findViewById(R.id.textViewUserRole);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        if(getIntent() != null && getIntent().hasExtra(KEY_PROFILE)){
            currentProfile = (Profile) getIntent().getSerializableExtra(KEY_PROFILE);

            textViewName.setText(currentProfile.getName());
            textViewEmail.setText(currentProfile.getEmail());
            textViewRole.setText(currentProfile.getRole());

        }

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProfileActivity.this, EditUserActivity.class);
                intent.putExtra(KEY_PROFILE, currentProfile);
                startEditActivityForResult.launch(intent);
            }
        });
    }

}
