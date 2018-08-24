package com.example.android.bakingapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.android.bakingapp.IdlingResource.SimpleIdlingResource;
import com.example.android.bakingapp.data.Ingredient;
import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.data.RecipeStep;
import com.example.android.bakingapp.utilities.JsonUtilities;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity
{

    private ArrayList<Recipe> mRecipeList;

    // for testing:]
    @Nullable
    private SimpleIdlingResource mSimpleIdlingResource;

    private void loadRecipes() {
        // retrieve our data and feed it to
        Executor networkExecutor = Executors.newSingleThreadExecutor();
        final Context context = this; // so that we can use in the async call

        // need to let espresso know we're idling.
        if (mSimpleIdlingResource != null) {
            mSimpleIdlingResource.setIdleState(false);
        } else {
            Timber.i("IdlingResource: null!");
        }

        networkExecutor.execute(new Runnable() {
            @Override
            public void run() {

                mRecipeList = JsonUtilities.retrieveRecipes();

                // now let's see if the fragment is ready for us
                final RecipesFragment fragment = (RecipesFragment) getSupportFragmentManager().findFragmentById(R.id.recipe_list_container);

                if (null != fragment) {
                    // have data, send to the fragment
                    // wow, this is a lot of work!
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Timber.d("Trying to load on fragment");
                            fragment.setRecipes(mRecipeList);
                            Timber.i("recipes: %s",(null == mRecipeList) ? "null" : mRecipeList.toString());
                            if (mSimpleIdlingResource != null) {
                                mSimpleIdlingResource.setIdleState(true);
                            }
                        }
                    });


                }
            }
        });
    }

    /**
     * in order to test better, set up the network call here so we
     * can get the idling resource set up first.
     */
    @Override
    public void onResume() {
        super.onResume();

        Timber.i("Idling Resource is null: %s", String.valueOf(mSimpleIdlingResource == null));

        // attempt to make the loading call here if we can.
        if (null == mRecipeList) {
            loadRecipes();
        }
    }

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

        RecipesFragment recipesFragment = RecipesFragment.newInstance(mRecipeList);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_list_container, recipesFragment)
                .commit();

        getSimpleIdlingResource();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @VisibleForTesting
    @NonNull
    public SimpleIdlingResource getSimpleIdlingResource() {
        Timber.i("getSimpleIdlingResource()");
        if (null == mSimpleIdlingResource) {
            mSimpleIdlingResource = new SimpleIdlingResource();
        }

        return mSimpleIdlingResource;
    }
}
