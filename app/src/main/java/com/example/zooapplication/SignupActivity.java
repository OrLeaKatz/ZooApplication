package com.example.zooapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void onSignupButtonClicked(View view) {
        String email = getEmail();
        String password = getPassword();

        if(email.isEmpty()) {
            /// show error message
            Toast.makeText(this, "Email must not be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!Utils.isValidEmail(email)) {
            Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length() < 7) {
            // show erro message
            return;
        }

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    // sign up successful
                    Toast.makeText(SignupActivity.this, "signup successful", Toast.LENGTH_SHORT).show();
                } else {
                    // sign up unsuccessful message
                }
            }
        });
    }

    private String getPassword() {
        AppCompatEditText passwordEditText = findViewById(R.id.passwordEditText);

        return passwordEditText.getText().toString().trim();
    }

    private String getEmail() {
        AppCompatEditText emailEditText = findViewById(R.id.emailEditText);

        return emailEditText.getText().toString().trim();
    }
}