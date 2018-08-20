package com.example.android.bakingapp.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.sqlite.SQLiteConstraintException;

import com.example.android.bakingapp.data.Recipe;

import java.util.List;

@Dao
public abstract class RecipeDao {
    @Insert(onConflict = OnConflictStrategy.FAIL)
    public abstract void insertRecipe(Recipe recipe);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void updateRecipe(Recipe recipe);

    @Delete
    public abstract void deleteRecipe(Recipe recipe);

    @Query("SELECT * FROM recipes")
    public abstract List<Recipe> getAllRecipes();

    @Query("SELECT * FROM recipes where id = :id")
    public abstract Recipe getRecipe(long id);

    /**
     * https://stackoverflow.com/questions/45677230/android-room-persistence-library-upsert
     * used so that when marking movies as favorites, it will insert the new movie if
     * it is not there yet. Else it'll update.
     */
    public void upsertRecipe(Recipe recipe) {
        try {
            insertRecipe(recipe);
        } catch (SQLiteConstraintException e) {
            updateRecipe(recipe);
        }
    }
}
