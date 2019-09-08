package com.example.zooapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class DisplayAnimalActivity extends AppCompatActivity {
    public static final String ANIMAL_ID_KEY = "KEY_ANIMAL_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_animal);

        long animalId = getIntent().getLongExtra(ANIMAL_ID_KEY, -1);

    }
}
