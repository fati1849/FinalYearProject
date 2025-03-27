package com.example.fithit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class RecommendationActivity extends AppCompatActivity {

    private TextView recommendationTitle, bulletPoint1, bulletPoint2, bulletPoint3, bulletPoint4;
    private Button homeButton; // Declare the "Go to Home" button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);

        // Bind UI elements
        recommendationTitle = findViewById(R.id.recommendationTitle);
        bulletPoint1 = findViewById(R.id.bulletPoint1);
        bulletPoint2 = findViewById(R.id.bulletPoint2);
        bulletPoint3 = findViewById(R.id.bulletPoint3);
        bulletPoint4 = findViewById(R.id.bulletPoint4);
        homeButton = findViewById(R.id.homeButton); // Bind the "Go to Home" button

        // Set up click listener for the "Go to Home" button
        homeButton.setOnClickListener(v -> {
            // Navigate to the HomeActivity
            Intent intent = new Intent(RecommendationActivity.this, HomeActivity.class);
            startActivity(intent);
        });

        // Get the USER_ID passed from the previous activity
        String userId = getIntent().getStringExtra("USER_ID");

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "User ID not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Reference Firebase Realtime Database
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        // Fetch data for the specific user
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Retrieve user data from Firebase
                    String heightStr = snapshot.child("height").getValue(String.class);
                    String weightStr = snapshot.child("weight").getValue(String.class);
                    String ageStr = snapshot.child("age").getValue(String.class);
                    String diseases = snapshot.child("diseases").getValue(String.class);
                    String gender = snapshot.child("gender").getValue(String.class);
                    String goal = snapshot.child("goal").getValue(String.class);

                    if (heightStr != null && weightStr != null && ageStr != null && gender != null && goal != null) {
                        // Parse numerical values
                        float height = Float.parseFloat(heightStr);
                        float weight = Float.parseFloat(weightStr);
                        float age = Float.parseFloat(ageStr);

                        // Calculate BMI
                        float bmi = weight / ((height / 100) * (height / 100));

                        // Make predictions using the TensorFlow Lite model
                        makePrediction(height, weight, age, bmi, diseases, gender, goal);
                    } else {
                        Toast.makeText(RecommendationActivity.this, "Incomplete user data!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RecommendationActivity.this, "No data found for this user!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(RecommendationActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Function to make predictions using the TensorFlow Lite model
    private void makePrediction(float height, float weight, float age, float bmi, String diseases, String gender, String goal) {
        try {
            // Load the TensorFlow Lite model
            RecommendationModelHandler modelHandler = new RecommendationModelHandler(this, "workout_recommendation_model.tflite");

            // Prepare input data (ensure alignment with training input features)
            float genderEncoded = encodeGender(gender); // Encode gender as numerical input
            float goalEncoded = encodeGoal(goal); // Encode goal as numerical input

            float[] inputData = {height, weight, age, bmi, genderEncoded, goalEncoded};

            // Run model inference
            float[] output = modelHandler.predict(inputData);

            // Process model output (find the class with the highest probability)
            int recommendedClassIndex = 0;
            float maxProbability = output[0];
            for (int i = 1; i < output.length; i++) {
                if (output[i] > maxProbability) {
                    maxProbability = output[i];
                    recommendedClassIndex = i;
                }
            }

            // Get recommendation details
            String exerciseRecommendation = getExerciseRecommendation(recommendedClassIndex);
            String dietPlan = getDietPlan(recommendedClassIndex);
            String equipment = getEquipment(recommendedClassIndex);
            String exerciseSets = getExerciseSets(recommendedClassIndex);

            // Populate TextViews with recommendations
            bulletPoint1.setText("Exercise Recommendation: " + exerciseRecommendation);
            bulletPoint2.setText("Diet Plan: " + dietPlan);
            bulletPoint3.setText("Equipment Needed: " + equipment);
            bulletPoint4.setText("Custom Exercise Sets: " + exerciseSets);

            // Close the model handler
            modelHandler.close();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load model", Toast.LENGTH_SHORT).show();
        }
    }

    // Helper function to encode gender into numerical values
    private float encodeGender(String gender) {
        if (gender.equalsIgnoreCase("male")) {
            return 0.0f; // Male = 0
        } else if (gender.equalsIgnoreCase("female")) {
            return 1.0f; // Female = 1
        }
        return -1.0f; // Unknown/Default
    }

    // Helper function to encode goal into numerical values
    private float encodeGoal(String goal) {
        switch (goal.toLowerCase()) {
            case "weight loss": return 0.0f;
            case "muscle gain": return 1.0f;
            case "general fitness": return 2.0f;
            default: return -1.0f; // Unknown/Default
        }
    }

    // Function to map class index to exercise recommendations
    private String getExerciseRecommendation(int classIndex) {
        switch (classIndex) {
            case 0: return "4 sets of 10-15 reps for each exercise.";
            case 1: return "3 sets of 12-15 reps for each exercise.";
            case 2: return "5 sets of 8-10 reps for each exercise.";
            case 3: return "2 sets of 15-20 reps for each exercise.";
            case 4: return "3 sets of 10-12 reps for each exercise.";
            default: return "No recommendation available.";
        }
    }

    // Function to map class index to diet plans
    private String getDietPlan(int classIndex) {
        switch (classIndex) {
            case 0: return "High protein, low carb diet.";
            case 1: return "Balanced diet with moderate carbs.";
            case 2: return "Low-fat, high-fiber diet.";
            case 3: return "Protein shakes and clean eating.";
            case 4: return "Mediterranean-style diet.";
            default: return "No diet plan available.";
        }
    }

    // Function to map class index to equipment
    private String getEquipment(int classIndex) {
        switch (classIndex) {
            case 0: return "Dumbbells, Barbell, Bench.";
            case 1: return "Kettlebells, Resistance Bands.";
            case 2: return "Pull-up Bar, Yoga Mat.";
            case 3: return "Treadmill, Elliptical.";
            case 4: return "Dumbbells, Medicine Ball.";
            default: return "No equipment needed.";
        }
    }

    // Function to map class index to custom exercise sets
    private String getExerciseSets(int classIndex) {
        switch (classIndex) {
            case 0: return "4 sets of 10-15 reps.";
            case 1: return "3 sets of 12-15 reps.";
            case 2: return "5 sets of 8-10 reps.";
            case 3: return "2 sets of 15-20 reps.";
            case 4: return "3 sets of 10-12 reps.";
            default: return "No custom sets available.";
        }
    }
}
