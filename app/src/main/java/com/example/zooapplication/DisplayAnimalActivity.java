package com.example.zooapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.widget.ImageViewCompat;
import data.Animal;
import data.DataManager;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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

        int animalId = getIntent().getIntExtra(ANIMAL_ID_KEY, -1);
        initAnimal(animalId);
    }

    public void onDescriptionEditButtonClicked(final View v) {
        Utils.showToastLong("Uploading animal entry to database..", this);
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

    private void initAnimal(final int animalId) {
        databaseReference.child("user_animals").child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String animalIdString = String.valueOf(animalId);

                if(dataSnapshot.hasChild(animalIdString)) { // user saved their own version of animal description
                    animal = dataSnapshot.child(animalIdString).getValue(Animal.class);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            setDataToLayout(animal);
                        }
                    });
                } else {
                    // get animal description from wiki api
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                animal = DataManager.animals[animalId];
                                final String animalDescription = getAnimalDescriptionFromWikiAPI(animal);
                                animal.description = animalDescription;
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        setDataToLayout(animal);
                                    }
                                });
                            } catch(IOException e) {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Utils.showToastLong("Could not send request, please check your internet connection.", DisplayAnimalActivity.this);
                                    }
                                });
                            } catch(JSONException e) {
                                // show toast for invalid json..
                            }
                        }
                    }).start();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String getAnimalDescriptionFromWikiAPI(Animal animal) throws IOException, JSONException {
        String wikiServerResponse = Utils.sendHttpRequest(animal.getWikiAPIUrl());

        JSONObject jsonObject = new JSONObject(wikiServerResponse);
        JSONObject pages = jsonObject.getJSONObject("query").getJSONObject("pages");

        if(!pages.keys().hasNext()) {
            // no page id found
            throw new JSONException("No page id.");
        } else {
            String pageIdString = pages.keys().next();
            String animalDescription = pages.getJSONObject(pageIdString).getString("extract");
            return animalDescription;
        }
    }



    private void setDataToLayout(Animal animal) {
        getDescriptionEditText().requestFocus();

        getEditFloatingActionButton().setEnabled(true);
        getSupportActionBar().setTitle(animal.name);

        if(animal.description != null) {
            getDescriptionEditText().setText(animal.description);
        }

        getAnimalImageView().setImageDrawable(Utils.getDrawableByName(animal.getResourceName(), this));
        Utils.hideSoftKeyboard(this); // hide soft keyboard which opened after request focus
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
