package com.example.android.bakingapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.utilities.ObjectConverter;

import timber.log.Timber;

@Database(entities = {Recipe.class}, version = 2)
@TypeConverters({ObjectConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "baking_recipes";

    // used to sync the instance code to avoid making 2 instances in a race condition.
    private static final Object LOCK = new Object();

    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Timber.d( "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        Timber.d( "Getting the database instance");
        return sInstance;
    }
    public abstract RecipeDao RecipeDao();
}
