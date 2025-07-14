package com.example.fithit;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ActivityStatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_status);

        //  Find the Complete Workout button
        Button btnCompleteWorkout = findViewById(R.id.btnCompleteWorkout);

        btnCompleteWorkout.setOnClickListener(v -> {
            //  Save that workout is completed
            getSharedPreferences("workoutPrefs", MODE_PRIVATE)
                    .edit()
                    .putBoolean("workoutCompletedToday", true)
                    .apply();

            //  Show confirmation
            Toast.makeText(ActivityStatusActivity.this,
                    "Workout marked as completed!",
                    Toast.LENGTH_SHORT).show();
        });

        // Existing code for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
