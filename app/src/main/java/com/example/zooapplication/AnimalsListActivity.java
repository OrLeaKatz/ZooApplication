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
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


public class AnimalsListActivity extends AppCompatActivity {
    private  ListView listView;
    private Animal[] initialAnimals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animals_list);
        listView = findViewById(R.id.animalListView);
        initListView();
    }

    private void initListView() {
        initialAnimals = Arrays.copyOf(DataManager.animals, DataManager.animals.length);

        Arrays.sort(initialAnimals, new Comparator<Animal>() {
            @Override
            public int compare(Animal animal1, Animal animal2) {
                return animal1.name.compareTo(animal2.name);
            }
        });

        List<String> animalNameList = new ArrayList<>();

        for(Animal animal : initialAnimals) {
            animalNameList.add(animal.name);
        }

        AlternatingColorArrayAdapter<String> arrayAdapter
                = new AlternatingColorArrayAdapter<>(this, android.R.layout.simple_list_item_1, animalNameList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int animalId = initialAnimals[i].id;
                Intent intent = new Intent(AnimalsListActivity.this, DisplayAnimalActivity.class);
                intent.putExtra(DisplayAnimalActivity.ANIMAL_ID_KEY, animalId);
                startActivity(intent);
            }
        });
    }
}
