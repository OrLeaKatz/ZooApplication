package com.example.zooapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import data.Animal;
import data.DataManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void onSignupButtonClicked(View view) {
        String email = getEmail();
        String password = getPassword();

        if (email.isEmpty()) {
            /// show error message
            Toast.makeText(this, "Email must not be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Utils.isValidEmail(email)) {
            Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 7) {
            // show error message
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // sign up successful
                    Toast.makeText(SignupActivity.this, "signup successful", Toast.LENGTH_SHORT).show();
                    uploadAnimalsForUser();
                    Intent intent = new Intent(SignupActivity.this, AnimalsListActivity.class);
                    startActivity(intent);
                } else {
                    // sign up unsuccessful message
                }
            }
        });
    }

    private String getPassword () {
        AppCompatEditText passwordEditText = findViewById(R.id.passwordEditText);

        return passwordEditText.getText().toString().trim();
    }

    private String getEmail () {
        AppCompatEditText emailEditText = findViewById(R.id.emailEditText);

        return emailEditText.getText().toString().trim();
    }

    private void uploadAnimalsForUser() {
        if(firebaseAuth.getUid() == null) {
            // show error, go to log in activity
            Toast.makeText(this, "not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        Animal[] initialAnimals = DataManager.getInitialAnimals(this);

        for(Animal animal : initialAnimals) {
            databaseReference.child("user_animals").child(firebaseAuth.getUid()).child(String.valueOf(animal.id)).setValue(animal);
        }

    }
}

