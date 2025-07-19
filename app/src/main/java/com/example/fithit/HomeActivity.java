package com.example.fithit;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1001;
    private TextView greeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Request Notification Permission on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_REQUEST_CODE
                );
            }
        }

        // Create Notification Channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "fitness_channel",
                    "Fitness Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Notifications for workout reminders");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        // Schedule the 4 daily notifications
        NotificationScheduler.scheduleNotifications(this);

        // UI setup
        greeting = findViewById(R.id.greeting1);
        TextView workoutDescription = findViewById(R.id.workoutDescription);
        ImageView workoutImage = findViewById(R.id.middleImage);
        TextView dateText = findViewById(R.id.dateText);
        ImageView bellIcon = findViewById(R.id.bellIcon);

        Button btnBeginner = findViewById(R.id.btnBeginner);
        Button btnIntermediate = findViewById(R.id.btnIntermediate);
        Button btnAdvanced = findViewById(R.id.btnAdvanced);

        // Dynamic username loading
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance()
                    .getReference("Users")
                    .child(currentUser.getUid());

            userRef.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    String userName = snapshot.getValue(String.class);
                    greeting.setText(userName != null ? userName : "Welcome");
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    greeting.setText("Welcome");
                }
            });
        } else {
            greeting.setText("Welcome");
        }

        // UI content
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        dateText.setText(dateFormat.format(currentDate));
        workoutDescription.setText("Day 1 - Cardio");
        workoutImage.setImageResource(R.drawable.sample_image);

        btnBeginner.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                Toast.makeText(HomeActivity.this, "You're not signed in — redirecting to Splash", Toast.LENGTH_SHORT).show();
                Intent splashIntent = new Intent(HomeActivity.this, SplashActivity.class);
                splashIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(splashIntent);
                finish();
            } else {
                Toast.makeText(HomeActivity.this, "Beginner level selected — showing recommendations", Toast.LENGTH_SHORT).show();
                Intent recommendIntent = new Intent(HomeActivity.this, RecommendationActivity.class);
                recommendIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(recommendIntent);
            }
        });

        btnIntermediate.setOnClickListener(v ->
                Toast.makeText(HomeActivity.this, "Intermediate level selected!", Toast.LENGTH_SHORT).show()
        );

        btnAdvanced.setOnClickListener(v ->
                Toast.makeText(HomeActivity.this, "Advanced level selected!", Toast.LENGTH_SHORT).show()
        );

        bellIcon.setOnClickListener(v ->
                Toast.makeText(HomeActivity.this, "Bell icon clicked!", Toast.LENGTH_SHORT).show()
        );

        // Bottom navigation logic — no RecommendationActivity here anymore
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                Toast.makeText(HomeActivity.this, "You're already on Home 🏠", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.navigation_workouts) {
                Toast.makeText(HomeActivity.this, "Workouts selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.navigation_settings) {
                Toast.makeText(HomeActivity.this, "Settings selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.navigation_gamification) {
                Toast.makeText(HomeActivity.this, "Gamification selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.navigation_person) {
                Toast.makeText(HomeActivity.this, "Profile selected", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification permission granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Notification permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}