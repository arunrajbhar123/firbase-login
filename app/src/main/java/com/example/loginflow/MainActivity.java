package com.example.loginflow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.loginflow.ui.Home;
import com.example.loginflow.ui.Login;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button loginActivity, profile;

    FirebaseAuth auth;
    TextView loginStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginActivity = findViewById(R.id.loginActivity);
        profile = findViewById(R.id.profile);
        loginStatus = findViewById(R.id.loginStatus);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            loginStatus.setText(String.format("%s User is Login", auth.getCurrentUser().getDisplayName()));
        } else {
            loginStatus.setText("User is not Login");
        }

        loginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(Login.class);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(Home.class);
            }
        });


    }

    private void changeActivity(Class activityClass) {
        Intent intent = new Intent(MainActivity.this, activityClass);
        startActivity(intent);
    }


}