package com.example.fithit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;



public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d("USER_UID", "UID is: " + user.getUid());
        } else {
            Log.d("USER_UID", "User not logged in");
        }

        Button btnGamification = findViewById(R.id.btnGamification);
        btnGamification.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, BadgesActivity.class);
            startActivity(intent);
        });




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
        btnBeginner.setOnClickListener(v -> {
            Toast.makeText(HomeActivity.this, "Beginner level selected!", Toast.LENGTH_SHORT).show();
            updateExerciseCount(); // <-- this is new
        });

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
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }

            else if(itemId == R.id.navigation_gamification) {
                Intent intent = new Intent(HomeActivity.this, BadgesActivity.class);
                startActivity(intent);
                return true;

        }
            else if (itemId == R.id.navigation_person) {  // ✅ your Community option
                Intent intent = new Intent(HomeActivity.this, CommunityActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
    private void updateExerciseCount() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);
            userRef.child("exercisesCompleted").setValue(ServerValue.increment(1));
        }
    }

}

