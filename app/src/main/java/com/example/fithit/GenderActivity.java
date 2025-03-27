package com.example.fithit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GenderActivity extends AppCompatActivity {

    private ImageView maleIcon;
    private ImageView femaleIcon;
    private Button nextButton;

    private DatabaseReference databaseReference;
    private String selectedGender = "";
    private String userId; // To store the user ID passed from the previous activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender);

        // Initialize Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference();

        maleIcon = findViewById(R.id.maleIcon);
        femaleIcon = findViewById(R.id.femaleIcon);
        nextButton = findViewById(R.id.nextButton);

        // Retrieve the user ID passed from HeightDiseasesActivity
        Intent intent = getIntent();
        userId = intent.getStringExtra("USER_ID");

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "User ID is missing. Cannot save gender.", Toast.LENGTH_LONG).show();
            finish(); // Close this activity as it can't proceed without a valid user ID
            return;
        }

        // Handle Male Icon Click
        maleIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedGender = "Male";
                Toast.makeText(GenderActivity.this, "Male selected", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Female Icon Click
        femaleIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedGender = "Female";
                Toast.makeText(GenderActivity.this, "Female selected", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Next Button Click
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedGender.isEmpty()) {
                    Toast.makeText(GenderActivity.this, "Please select a gender", Toast.LENGTH_SHORT).show();
                } else {
                    // Update the gender in the existing user record
                    databaseReference.child("Users").child(userId).child("gender").setValue(selectedGender)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(GenderActivity.this, "Gender saved successfully", Toast.LENGTH_SHORT).show();

                                    // Proceed to GoalInfoActivity
                                    Intent goalIntent = new Intent(GenderActivity.this, goalInfoActivity.class);
                                    goalIntent.putExtra("USER_ID", userId); // Pass the user ID forward
                                    startActivity(goalIntent);
                                } else {
                                    Toast.makeText(GenderActivity.this, "Failed to save gender.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
}
