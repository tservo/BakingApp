package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.data.RecipeStep;
import com.example.android.bakingapp.database.AppDatabase;
import com.example.android.bakingapp.database.RecipeDao;
import com.example.android.bakingapp.utilities.PreferencesHelper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import timber.log.Timber;

import static com.example.android.bakingapp.RecipeStepDetailActivity.ARG_RECIPE_STEP_POSITION;

public class RecipeDetailActivity extends AppCompatActivity
    implements RecipeStepItemsAdapter.RecipeStepItemsClickListener {

    private boolean mTwoPane; // do we have the recipe and step detail activities in the same place?

    private int mStepPosition; // store the recipe step here.
    private Recipe mRecipe; // store the recipe associated with the activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // let's determine if we're in a two-pane situation
        mTwoPane = isTwoPane();

        // and if we changed orientation, stay on the same step too.
        if (null != savedInstanceState) {
            mStepPosition = savedInstanceState.getInt(ARG_RECIPE_STEP_POSITION, 0);
        } else {
            mStepPosition = 0;
        }

        // get info from intent and put into fragment
        Intent intent = getIntent();
        if (intent.hasExtra(RecipeDetailFragment.ARG_RECIPE)) {
            mRecipe = intent.getParcelableExtra(RecipeDetailFragment.ARG_RECIPE);
            Timber.d("Recipe: %s",mRecipe.toString());
        }


        // this should be in a view model but i don't have time to do so
        // store it in the db
        final AppDatabase database = AppDatabase.getInstance(this);
        Executor dbExecutor = Executors.newSingleThreadExecutor();

        // maybe we came from the widget -- pull the current recipe then.
        if (null == mRecipe) {
            final long id = PreferencesHelper.getCurrentRecipeId(this);
            dbExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    mRecipe = database.RecipeDao().getRecipe(id);
                }
            });

        } else {
            // we received our recipe -- make sure it's in the database
            dbExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    database.RecipeDao().upsertRecipe(mRecipe);
                }
            });
            // mark this recipe as current:
            PreferencesHelper.setCurrentRecipe(this, mRecipe);
        }





        // create the fragment and put it into the activity
        FragmentManager fragmentManager = getSupportFragmentManager();

        // add the detail fragment, always.
        RecipeDetailFragment recipeDetailFragment = RecipeDetailFragment.newInstance(mRecipe);
        fragmentManager.beginTransaction()
                .replace(R.id.recipe_detail_container,recipeDetailFragment)
                .commit();

        // and the step detail fragment if we need it
        if (mTwoPane) {
            // put up the first step as a default.
            RecipeStep recipeStep = null;
            if (null != mRecipe) {
                recipeStep = mRecipe.getStep(mStepPosition);
            }

            createRecipeStepDetailFragment(fragmentManager, recipeStep);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // peform some actions on the toolbar.
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(getString(R.string.RECIPE_TEXT) + " " + mRecipe.getName());

    }

    /**
     * helper method to set up the recipe step detail properly.
     * @param fragmentManager
     * @param firstStep
     */
    private void createRecipeStepDetailFragment(FragmentManager fragmentManager, RecipeStep firstStep) {
        RecipeStepDetailFragment recipeStepDetailFragment = RecipeStepDetailFragment.newInstance(firstStep);
        recipeStepDetailFragment.setViewMode(RecipeStepDetailFragment.ViewMode.TWO_PANE);

        fragmentManager.beginTransaction()
            .replace(R.id.recipe_step_detail_container,recipeStepDetailFragment)
            .commit();
    }

    /**
     * utility method to determine if there is two panels or not
     * @return are we two pane?
     */
    private boolean isTwoPane() {
        return (findViewById(R.id.recipe_step_detail_container) != null);
    }

    /**
     * handle the recipeStep click. It'll either be an intent
     * or if a two-pane, replace the other fragment!
     * @param recipeStep - use for the fragment.
     * @param clickPosition the position we're at -- used for the paging on the next activity
     */
    @Override
    public void onRecipeStepItemClick(RecipeStep recipeStep, int clickPosition) {
        mStepPosition = clickPosition;
        if (mTwoPane) {
            // make a step detail fragment!
            createRecipeStepDetailFragment(getSupportFragmentManager(),recipeStep);
        } else {
            // send an intent!
            Intent intent = new Intent(this, RecipeStepDetailActivity.class);
            intent.putExtra(RecipeDetailFragment.ARG_RECIPE, mRecipe);
            intent.putExtra(ARG_RECIPE_STEP_POSITION, clickPosition);
            startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Timber.d("I'm being saved: instance state!");
        outState.putInt(ARG_RECIPE_STEP_POSITION, mStepPosition);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

}
