package com.example.fithit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SignActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        Button signupButton = findViewById(R.id.signupButton);
        TextView loginText = findViewById(R.id.loginText);

        // Handle Sign-Up Button Click
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        // Handle Log In Text Click
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
