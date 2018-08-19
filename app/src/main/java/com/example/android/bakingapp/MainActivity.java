package com.example.android.bakingapp;

import android.app.Activity;
import android.app.Application;
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

    //private final MyActivityLifecycleCallbacks mCallbacks = new MyActivityLifecycleCallbacks();

    public static class MyActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Timber.i("Class %s: object %s: call %s", activity.getClass().getSimpleName(), activity.toString(),  "onCreate(Bundle)");
        }

        @Override
        public void onActivityStarted(Activity activity) {
            Timber.i("Class %s: object %s: call %s", activity.getClass().getSimpleName(), activity.toString(),  "onStart()");
        }

        @Override
        public void onActivityResumed(Activity activity) {
            Timber.i("Class %s: object %s: call %s", activity.getClass().getSimpleName(), activity.toString(),  "onResume()");
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Timber.i("Class %s: object %s: call %s", activity.getClass().getSimpleName(), activity.toString(),  "onPause()");
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            Timber.i("Class %s: object %s: call %s", activity.getClass().getSimpleName(), activity.toString(),  "onSaveInstanceState(Bundle)");
        }

        @Override
        public void onActivityStopped(Activity activity) {
            Timber.i("Class %s: object %s: call %s", activity.getClass().getSimpleName(), activity.toString(),  "onStop()");
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Timber.i("Class %s: object %s: call %s", activity.getClass().getSimpleName(), activity.toString(),  "onDestroy()");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // debugging for the up button crashing on recipedetailactivity
        //getApplication().registerActivityLifecycleCallbacks(mCallbacks);


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


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_list_container, recipesFragment)
                .commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //getApplication().unregisterActivityLifecycleCallbacks(mCallbacks);

    }
}
