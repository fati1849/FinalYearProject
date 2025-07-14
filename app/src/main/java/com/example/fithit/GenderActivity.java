package com.example.fithit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GenderActivity extends AppCompatActivity {

    private ImageView maleIcon;
    private ImageView femaleIcon;
    private Button nextButton;

    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private String selectedGender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender);

        // Initialize Firebase
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        String uid = currentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid);

        // Bind UI
        maleIcon = findViewById(R.id.maleIcon);
        femaleIcon = findViewById(R.id.femaleIcon);
        nextButton = findViewById(R.id.nextButton);

        // Handle Male Icon Click
        maleIcon.setOnClickListener(v -> {
            selectedGender = "Male";
            Toast.makeText(GenderActivity.this, "Male selected", Toast.LENGTH_SHORT).show();
        });

        // Handle Female Icon Click
        femaleIcon.setOnClickListener(v -> {
            selectedGender = "Female";
            Toast.makeText(GenderActivity.this, "Female selected", Toast.LENGTH_SHORT).show();
        });

        // Handle Next Button Click
        nextButton.setOnClickListener(v -> {
            if (selectedGender.isEmpty()) {
                Toast.makeText(GenderActivity.this, "Please select a gender", Toast.LENGTH_SHORT).show();
            } else {
                // Save gender to Realtime Database
                databaseReference.child("gender").setValue(selectedGender)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(GenderActivity.this, "Gender saved successfully", Toast.LENGTH_SHORT).show();

                                // Proceed to next activity
                                Intent intent = new Intent(GenderActivity.this, goalInfoActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(GenderActivity.this, "Failed to save gender", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
