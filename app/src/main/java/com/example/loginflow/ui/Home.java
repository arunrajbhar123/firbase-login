package com.example.loginflow.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.loginflow.databinding.ActivityHomeBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity {

    FirebaseAuth auth;
    ActivityHomeBinding binding;

    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        googleSignInClient = GoogleSignIn.getClient(Home.this, GoogleSignInOptions.DEFAULT_SIGN_IN);


        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null || auth == null) {
            redirectToLogin();
            return;
        }

        FirebaseUser user = auth.getCurrentUser();
        binding.name.setText(String.format("Name : %s", user.getDisplayName()));
        binding.email.setText(String.format("Email : " + user.getEmail()));

        user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (task.isSuccessful()) {
                    binding.token.setText("Token " + ":" + task.getResult().getToken());
                } else {
                    System.out.println("Token : null ");
                }
            }
        });


        Picasso.get().load(user.getPhotoUrl()).into(binding.userPhoto);

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            auth.signOut();
                            redirectToLogin();
                        }
                    }
                });


            }
        });


        binding.copyToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied Text", binding.token.getText());

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