package com.example.android.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.bakingapp.data.Ingredient;
import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.utilities.JsonUtilities;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity
{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // install Timber Tree
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());

            // add the stetho diagnostic tools
            Stetho.initializeWithDefaults(this);
            new OkHttpClient.Builder()
                    .addNetworkInterceptor(new StethoInterceptor())
                    .build();
        }

        setContentView(R.layout.activity_main);


        // add our fragments
        ArrayList<Recipe> recipes = JsonUtilities.retrieveRecipes(this);
        RecipesFragment recipesFragment = RecipesFragment.newInstance(recipes);

        // if twopane, add recipedetails fragment
        // RecipeDetailsFragment recipeDetailFragment = RecipeDetailFragment.newInstance(recipe);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_list_container, recipesFragment)
                .commit();

    }


}
