package com.example.android.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.bakingapp.data.Ingredient;
import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.utilities.JsonUtilities;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.List;

import okhttp3.OkHttpClient;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

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
        RecipesFragment recipesFragment = new RecipesFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_list_container, recipesFragment)
                .commit();

        // load up information:
        // get JSON link
        //
        // -> JSON
        // USE gson to handle the resulting JSON
        List<Recipe> recipes =JsonUtilities.retrieveRecipes(this);

        for(int i=0; i < recipes.size(); i++) {
            Recipe recipe = recipes.get(i);
            Timber.d("Name of recipe: %s",recipe.getName());
            List<Ingredient> ingredients = recipe.getIngredients();
            for (int j=0; j < ingredients.size(); j++) {
                Ingredient ingredient = ingredients.get(j);
                Timber.d("Name of ingredient: %s", ingredient.getIngredient());
            }

        }
    }
}
