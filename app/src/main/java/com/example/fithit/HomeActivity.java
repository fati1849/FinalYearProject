package com.example.fithit;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Update dynamic views
        TextView greeting = findViewById(R.id.greeting1);
        TextView workoutDescription = findViewById(R.id.workoutDescription);
        ImageView workoutImage = findViewById(R.id.middleImage);
        TextView dateText = findViewById(R.id.dateText);
        ImageView bellIcon = findViewById(R.id.bellIcon); // Added bell icon view

        Button btnBeginner = findViewById(R.id.btnBeginner);
        Button btnIntermediate = findViewById(R.id.btnIntermediate);
        Button btnAdvanced = findViewById(R.id.btnAdvanced);

        // Set data dynamically
        String userName = "Zunaira";
        greeting.setText( userName);

        // Set dynamic current date
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        dateText.setText( dateFormat.format(currentDate));

        workoutDescription.setText("Day 1 - Cardio");
        workoutImage.setImageResource(R.drawable.sample_image);

        // Add button click handlers
        btnBeginner.setOnClickListener(v ->
                Toast.makeText(HomeActivity.this, "Beginner level selected!", Toast.LENGTH_SHORT).show()
        );

        btnIntermediate.setOnClickListener(v ->
                Toast.makeText(HomeActivity.this, "Intermediate level selected!", Toast.LENGTH_SHORT).show()
        );

        btnAdvanced.setOnClickListener(v ->
                Toast.makeText(HomeActivity.this, "Advanced level selected!", Toast.LENGTH_SHORT).show()
        );

        // Add click listener for the bell icon
        bellIcon.setOnClickListener(v ->
                Toast.makeText(HomeActivity.this, "Bell icon clicked!", Toast.LENGTH_SHORT).show()
        );

        // Bottom Navigation Bar setup
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                Toast.makeText(HomeActivity.this, "Home selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.navigation_workouts) {
                Toast.makeText(HomeActivity.this, "Workouts selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.navigation_settings) {
                Toast.makeText(HomeActivity.this, "Settings selected", Toast.LENGTH_SHORT).show();
                return true;
            }
            else if (itemId == R.id.navigation_gamification) {
                Toast.makeText(HomeActivity.this, "Gamification selected", Toast.LENGTH_SHORT).show();
                return true;
            }
            else if (itemId == R.id.navigation_person) {
                Toast.makeText(HomeActivity.this, "Profile selected", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }
}
