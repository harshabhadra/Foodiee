package com.example.android.foodie;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {BookMark.class}, version = 3, exportSchema = false)
public abstract class FoodDatabase extends RoomDatabase {

    public abstract FoodDao foodDao();
    private static FoodDatabase INSTANCE;

    static FoodDatabase getDatabase(Context context) {

        if (INSTANCE == null) {
            synchronized (FoodDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FoodDatabase.class, "food_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
