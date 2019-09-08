package com.example.zooapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent intent = new Intent(this, SignupActivity.class);
//        startActivity(intent);

        /* example -send animal id to dispaly animal activity */
        Intent intent1 = new Intent(this, DisplayAnimalActivity.class);
        long animalId = 1;
        intent1.putExtra(DisplayAnimalActivity.ANIMAL_ID_KEY, animalId);
        startActivity(intent1);
    }
}
