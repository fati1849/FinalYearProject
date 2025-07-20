package com.example.fithit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class goalInfoActivity extends AppCompatActivity {

    private RadioGroup goalGroup;
    private Button getStartedButton;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goalinfo);

        // Initialize Firebase references
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        String uid = currentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid);

        goalGroup = findViewById(R.id.goalGroup);
        getStartedButton = findViewById(R.id.getStartedButton);

        getStartedButton.setOnClickListener(v -> {
            int selectedId = goalGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(goalInfoActivity.this, "Please select a goal", Toast.LENGTH_SHORT).show();
            } else {
                RadioButton selectedRadioButton = findViewById(selectedId);
                String selectedGoal = selectedRadioButton.getText().toString();

                // Save selected goal
                databaseReference.child("goal").setValue(selectedGoal)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(goalInfoActivity.this, "Goal saved successfully", Toast.LENGTH_SHORT).show();

                                // Move to RecommendationActivity
                                Intent intent = new Intent(goalInfoActivity.this, RecommendationActivity.class);
                                startActivity(intent);
                                finish(); // Optional: prevent back navigation
                            } else {
                                Toast.makeText(goalInfoActivity.this, "Failed to save goal", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
