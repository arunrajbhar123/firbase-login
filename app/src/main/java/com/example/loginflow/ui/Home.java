package com.example.loginflow.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


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

        SharedPreferences sh = getSharedPreferences("token", MODE_PRIVATE);
        String idToken = sh.getString("token", "");

        token.setText("Token " + ":" + idToken);


        Picasso.get().load(user.getPhotoUrl()).into(photo);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                redirectToLogin();
            }
        });


        ImageView copyImage = findViewById(R.id.copyToken);

        copyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied Text", idToken);

                clipboard.setPrimaryClip(clip);
                Toast.makeText(Home.this, "Token copied", Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void redirectToLogin() {
        Intent intent = new Intent(Home.this, Login.class);
        startActivity(intent);
        finish();
    }
}