package data;

import android.content.Context;

import com.example.zooapplication.Utils;

import data.Animal;

public class DataManager {
    public static Animal[] getInitialAnimals(Context context) {
        return new Animal[] {
          new Animal(
                  0,
                  "Lion",
                  Utils.readStringFromRawResourceFile(context, "animal0")
          ),
          new Animal(
                  1,
                  "Zebra",
                  Utils.readStringFromRawResourceFile(context, "animal1")
          ),
          new Animal(
                  2,
                  "Cat",
                  Utils.readStringFromRawResourceFile(context, "animal2")
          )
        };
    }
}
