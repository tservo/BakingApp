package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.android.bakingapp.data.Recipe;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // get info from intent and put into fragment
        Intent intent = getIntent();
        Recipe recipe = null;
        if (intent.hasExtra(RecipeDetailFragment.ARG_RECIPE)) {
            recipe = intent.getParcelableExtra(RecipeDetailFragment.ARG_RECIPE);
        }

        // create the fragment and put it into the activity
        RecipeDetailFragment recipeDetailFragment = RecipeDetailFragment.newInstance(recipe);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_detail_container,recipeDetailFragment)
                .commit();


        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
