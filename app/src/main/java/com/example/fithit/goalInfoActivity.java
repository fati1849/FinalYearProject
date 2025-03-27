package com.example.fithit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class goalInfoActivity extends AppCompatActivity {

    private RadioGroup goalGroup;
    private Button getStartedButton;
    private DatabaseReference databaseReference;
    private String userId; // Store the user's unique ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goalinfo);

        // Initialize Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference();

        goalGroup = findViewById(R.id.goalGroup);
        getStartedButton = findViewById(R.id.getStartedButton);

        // Retrieve the user ID passed from the previous activity
        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getStringExtra("USER_ID");
        }

        // Check if the userId is null
        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "User ID is null or missing. Cannot save data!", Toast.LENGTH_LONG).show();
            return; // Prevent further actions if userId is null
        }

        getStartedButton.setOnClickListener(v -> {
            int selectedId = goalGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(goalInfoActivity.this, "Please select a goal", Toast.LENGTH_SHORT).show();
            } else {
                RadioButton selectedRadioButton = findViewById(selectedId);
                String selectedGoal = selectedRadioButton.getText().toString();

                // Save the selected goal to Firebase under the existing user ID
                databaseReference.child("Users").child(userId).child("goal").setValue(selectedGoal)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(goalInfoActivity.this, "Goal saved successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(goalInfoActivity.this, "Failed to save goal", Toast.LENGTH_SHORT).show();
                            }
                        });

                // Proceed to the next activity (e.g., DashboardActivity)
                Intent homeIntent = new Intent(goalInfoActivity.this, RecommendationActivity.class);
                startActivity(homeIntent);
            }
        });
    }
}
