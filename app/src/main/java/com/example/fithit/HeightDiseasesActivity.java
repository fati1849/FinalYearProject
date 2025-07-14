package com.example.fithit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HeightDiseasesActivity extends AppCompatActivity {

    private EditText heightInput, weightInput, ageInput;
    private Spinner diseasesSpinner;
    private Button nextButton;

    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height_diseases);

        // Initialize Firebase
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Bind UI
        heightInput = findViewById(R.id.heightInput);
        weightInput = findViewById(R.id.weightInput);
        ageInput = findViewById(R.id.ageInput);
        diseasesSpinner = findViewById(R.id.diseasesSpinner);
        nextButton = findViewById(R.id.nextButton);

        // Set up spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.diseases_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diseasesSpinner.setAdapter(adapter);

        nextButton.setOnClickListener(v -> {
            String heightStr = heightInput.getText().toString().trim();
            String weightStr = weightInput.getText().toString().trim();
            String age = ageInput.getText().toString().trim();
            String diseases = diseasesSpinner.getSelectedItem().toString().trim();

            if (heightStr.isEmpty() || weightStr.isEmpty() || age.isEmpty()) {
                Toast.makeText(this, "Please enter all the required information", Toast.LENGTH_SHORT).show();
                return;
            }

            if (currentUser == null) {
                Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                float height = Float.parseFloat(heightStr);
                float weight = Float.parseFloat(weightStr);
                float bmi = weight / ((height / 100) * (height / 100)); // BMI calculation

                String uid = currentUser.getUid();

                User user = new User(heightStr, weightStr, age, diseases, String.valueOf(bmi));

                // Store under UID
                databaseReference.child("Users").child(uid).setValue(user)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(this, GenderActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show();
                            }
                        });

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Enter valid numbers for height/weight", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Data model
    public static class User {
        public String height, weight, age, diseases, bmi;

        public User() {
        }

        public User(String height, String weight, String age, String diseases, String bmi) {
            this.height = height;
            this.weight = weight;
            this.age = age;
            this.diseases = diseases;
            this.bmi = bmi;
        }
    }
}
