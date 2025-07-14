package com.example.fithit;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class DashboardActivity extends AppCompatActivity {

    private TextView greetingText;  // TextView to show username

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        // Apply padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize greeting TextView
        greetingText = findViewById(R.id.greetingText);  // This must match the ID in XML

        // Load username from Firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();

            FirebaseDatabase.getInstance().getReference("Users").child(uid).child("username")
                    .get().addOnSuccessListener(snapshot -> {
                        if (snapshot.exists()) {
                            String username = snapshot.getValue(String.class);
                            greetingText.setText("Hello, " + username + "!");
                        } else {
                            greetingText.setText("Hello, User!");
                        }
                    }).addOnFailureListener(e -> {
                        greetingText.setText("Hello!");
                    });
        }
    }
}
