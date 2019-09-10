package com.example.zooapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.widget.ImageViewCompat;
import data.Animal;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DisplayAnimalActivity extends AppCompatActivity {
    public static final String ANIMAL_ID_KEY = "KEY_ANIMAL_ID";

    private Animal animal;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_animal);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        if(firebaseAuth.getCurrentUser() == null) {
            Utils.showToastLong("User not logged in", this);
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
            return;
        }

        getEditFloatingActionButton().setEnabled(false);

        long animalId = getIntent().getLongExtra(ANIMAL_ID_KEY, -1);
        initAnimal(animalId);
    }

    public void onDescriptionEditButtonClicked(final View v) {
        v.setEnabled(false);
        this.animal.description = getDescriptionEditText().getText().toString().trim();
        databaseReference.child("user_animals").child(firebaseAuth.getUid()).child(String.valueOf(animal.id))
                .setValue(animal).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                v.setEnabled(true);
                if((task.isSuccessful())) {
                    Utils.showToastLong("Animal updated successfully.", DisplayAnimalActivity.this);
                } else {
                    Utils.showToastLong("Error updating animal.", DisplayAnimalActivity.this);
                }
            }
        });
    }

    private void initAnimal(long animalId) {
        databaseReference.child("user_animals").child(firebaseAuth.getUid())
                .child(String.valueOf(animalId)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                animal =  dataSnapshot.getValue(Animal.class);

                if(animal != null) { // database is corrupt
                    setDataToLayout(animal);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setDataToLayout(Animal animal) {
        getSupportActionBar().setTitle(animal.name);
        getDescriptionEditText().setText(animal.description);
        getAnimalImageView().setImageDrawable(Utils.getDrawableByName(animal.getResourceName(), this));
        getEditFloatingActionButton().setEnabled(true);
    }

    private AppCompatImageView getAnimalImageView() {
        return findViewById(R.id.animalImageView);
    }
    private AppCompatEditText getDescriptionEditText() {
        return findViewById(R.id.descriptionEditText);
    }

    private FloatingActionButton getEditFloatingActionButton() {
        return findViewById(R.id.editFloatingActionButton);
    }
}
