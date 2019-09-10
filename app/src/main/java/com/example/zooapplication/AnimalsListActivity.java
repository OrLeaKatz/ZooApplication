package com.example.zooapplication;

import androidx.appcompat.app.AppCompatActivity;
import data.Animal;
import data.DataManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;


public class AnimalsListActivity extends AppCompatActivity {
    private  ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animals_list);
        listView = findViewById(R.id.animalListView);
        initListView();
    }

    private void initListView() {
        Animal[] initialAnimals = DataManager.getInitialAnimals(this);
        List<String> animalNameList = new ArrayList<>();

        for(Animal animal : initialAnimals) {
            animalNameList.add(animal.name);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, animalNameList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                long animalId = i;
                Intent intent = new Intent(AnimalsListActivity.this, DisplayAnimalActivity.class);
                intent.putExtra(DisplayAnimalActivity.ANIMAL_ID_KEY, animalId);
                startActivity(intent);
            }
        });
    }
}
