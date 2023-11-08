package com.example.loginflow.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.loginflow.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity {

    FirebaseAuth auth;
    TextView name, email, phone, token;
    ImageView photo;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        token = findViewById(R.id.token);
        photo = findViewById(R.id.userPhoto);
        logout = findViewById(R.id.logout);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null || auth == null) {
            redirectToLogin();
            return;
        }

        FirebaseUser user = auth.getCurrentUser();
        name.setText(String.format("Name : %s", user.getDisplayName()));
        email.setText(String.format("Email : " + user.getEmail()));
        phone.setText(String.format("Phone : " + user.getPhoneNumber()));
        token.setText("Token : " + user.getPhotoUrl());

        Picasso.get().load(user.getPhotoUrl()).into(photo);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                redirectToLogin();
            }
        });

    }


    private void redirectToLogin() {
        Intent intent = new Intent(Home.this, Login.class);
        startActivity(intent);
        finish();
    }
}