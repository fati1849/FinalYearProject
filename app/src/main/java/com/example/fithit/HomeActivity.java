package com.example.fithit;

<<<<<<< HEAD
import android.content.Intent;
=======
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
>>>>>>> 6464a7a447b93cba5637d791cca9425adfb955b3
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

<<<<<<< HEAD
=======
import androidx.annotation.NonNull;
>>>>>>> 6464a7a447b93cba5637d791cca9425adfb955b3
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

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

    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1001;
    private TextView greeting;

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

<<<<<<< HEAD
        // Add button click handlers
        btnBeginner.setOnClickListener(v -> {
            Toast.makeText(HomeActivity.this, "Beginner level selected!", Toast.LENGTH_SHORT).show();
            updateExerciseCount(); // <-- this is new
=======
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
>>>>>>> 6464a7a447b93cba5637d791cca9425adfb955b3
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
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
<<<<<<< HEAD
            }

            else if(itemId == R.id.navigation_gamification) {
                Intent intent = new Intent(HomeActivity.this, BadgesActivity.class);
                startActivity(intent);
                return true;

        }
            else if (itemId == R.id.navigation_person) {  // ✅ your Community option
                Intent intent = new Intent(HomeActivity.this, CommunityActivity.class);
                startActivity(intent);
=======
            } else if (itemId == R.id.navigation_gamification) {
                Toast.makeText(HomeActivity.this, "Gamification selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.navigation_person) {
                Toast.makeText(HomeActivity.this, "Profile selected", Toast.LENGTH_SHORT).show();
>>>>>>> 6464a7a447b93cba5637d791cca9425adfb955b3
                return true;
            }
            return false;
        });
    }
<<<<<<< HEAD
    private void updateExerciseCount() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);
            userRef.child("exercisesCompleted").setValue(ServerValue.increment(1));
        }
    }

}

=======

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
>>>>>>> 6464a7a447b93cba5637d791cca9425adfb955b3
