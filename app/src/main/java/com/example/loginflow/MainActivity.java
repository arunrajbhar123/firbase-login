package com.example.loginflow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.loginflow.databinding.ActivityMainBinding;
import com.example.loginflow.ui.Home;
import com.example.loginflow.ui.Login;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            binding.loginStatus.setText(String.format("%s User is Login", auth.getCurrentUser().getDisplayName()));
        } else {
            binding.loginStatus.setText("User is not Login");
        }

        binding.loginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(Login.class);
            }
        });

        binding.profile.setOnClickListener(new View.OnClickListener() {
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