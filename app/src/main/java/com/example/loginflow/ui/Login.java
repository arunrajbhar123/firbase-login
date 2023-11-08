package com.example.loginflow.ui;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.loginflow.R;
import com.example.loginflow.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity {

    FirebaseAuth auth;
    GoogleSignInClient mGoogleSignInClient;
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);


        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            FirebaseUser user = auth.getCurrentUser();
            changeActivity(user);


        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.loginBtn.setEnabled(false);
                Intent signIn = mGoogleSignInClient.getSignInIntent();
                launcher.launch(signIn);
            }
        });


    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            if (o.getResultCode() == Activity.RESULT_OK) {
                Intent data = o.getData();
                handleGoogleSignInResult(data);
                binding.showError.setVisibility(View.GONE);

            } else {
                showError("from launcher");
            }
        }


        private void handleGoogleSignInResult(Intent data) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    if (account.getIdToken() != null) {
                        firebaseAuth(account.getIdToken());
                    }
                } else {
                    showError("getting account null");
                }
            } catch (ApiException e) {
                showError("from ApiException");
            }
        }


        private void firebaseAuth(String idToken) {
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

            auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        FirebaseUser user = auth.getCurrentUser();
                        assert user != null;
                        changeActivity(user);

                    } else {
                        showError("from firebaseAuth");
                    }
                }
            });
        }
    });


    private void showError(String error) {
        binding.showError.setText("Something is wrong." + error);
        binding.showError.setVisibility(View.VISIBLE);
        binding.loginBtn.setEnabled(true);
        binding.progressBar.setVisibility(View.GONE);
    }

    private void changeActivity(FirebaseUser user) {

        Intent intent = new Intent(Login.this, Home.class);
        startActivity(intent);
        finish();
    }
}