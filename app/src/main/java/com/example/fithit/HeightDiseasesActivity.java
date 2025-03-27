package com.example.fithit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HeightDiseasesActivity extends AppCompatActivity {

    private EditText heightInput;
    private EditText weightInput;
    private EditText ageInput;
    private Spinner diseasesSpinner;
    private Button nextButton;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height_diseases);

        // Initialize Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Bind UI elements
        heightInput = findViewById(R.id.heightInput);
        weightInput = findViewById(R.id.weightInput);
        ageInput = findViewById(R.id.ageInput);
        diseasesSpinner = findViewById(R.id.diseasesSpinner);
        nextButton = findViewById(R.id.nextButton);

        // Set up the diseases dropdown
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.diseases_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diseasesSpinner.setAdapter(adapter);

        // Handle the Next button click
        nextButton.setOnClickListener(v -> {
            String heightStr = heightInput.getText().toString().trim();
            String weightStr = weightInput.getText().toString().trim();
            String age = ageInput.getText().toString().trim();
            String diseases = diseasesSpinner.getSelectedItem().toString().trim();

            if (heightStr.isEmpty() || weightStr.isEmpty() || age.isEmpty()) {
                Toast.makeText(HeightDiseasesActivity.this, "Please enter all the required information", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    // Parse height and weight to calculate BMI
                    float height = Float.parseFloat(heightStr);
                    float weight = Float.parseFloat(weightStr);
                    float bmi = weight / ((height / 100) * (height / 100)); // Calculate BMI

                    // Reference to the UserIdCounter
                    DatabaseReference counterRef = databaseReference.child("UserIdCounter");

                    // Retrieve the current value of the counter
                    counterRef.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Integer currentCounter = task.getResult().getValue(Integer.class);

                            if (currentCounter == null) {
                                currentCounter = 0; // Default to 0 if null
                            }

                            // Increment the counter for the next user
                            int newUserId = currentCounter + 1;

                            // Update the counter in the database
                            counterRef.setValue(newUserId);

                            // Create a User object
                            User user = new User(heightStr, weightStr, age, diseases, String.valueOf(bmi));

                            // Store the user data using the integer User ID
                            databaseReference.child("Users").child(String.valueOf(newUserId)).setValue(user)
                                    .addOnCompleteListener(userTask -> {
                                        if (userTask.isSuccessful()) {
                                            Toast.makeText(HeightDiseasesActivity.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(HeightDiseasesActivity.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                                        }

                                        // Always navigate to GenderActivity
                                        Intent intent = new Intent(HeightDiseasesActivity.this, GenderActivity.class);
                                        intent.putExtra("USER_ID", String.valueOf(newUserId)); // Pass USER_ID as String
                                        startActivity(intent);
                                    });

                        } else {
                            // Handle the case where UserIdCounter does not exist
                            counterRef.setValue(1).addOnCompleteListener(initTask -> {
                                if (initTask.isSuccessful()) {
                                    Toast.makeText(HeightDiseasesActivity.this, "Counter initialized. Proceeding to GenderActivity.", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(HeightDiseasesActivity.this, GenderActivity.class);
                                    intent.putExtra("USER_ID", "1"); // Default to User ID 1
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(HeightDiseasesActivity.this, "Failed to initialize User ID Counter", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

                } catch (NumberFormatException e) {
                    Toast.makeText(HeightDiseasesActivity.this, "Please enter valid numerical values for height and weight!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // User Data Model
    public static class User {
        public String height;
        public String weight;
        public String age;
        public String diseases;
        public String bmi; // New field for BMI

        // Default constructor required for Firebase
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
