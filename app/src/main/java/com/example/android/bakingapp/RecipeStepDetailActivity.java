package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.data.RecipeStep;

import java.util.ArrayList;

import timber.log.Timber;

public class RecipeStepDetailActivity extends AppCompatActivity {

    public static final String ARG_RECIPE_STEP_POSITION = "recipe_step_position";
    private Recipe mRecipe; // with the recipe, we can have the recipe name AND all steps for paging.
    private int mRecipeStepPosition; // the recipe step we're on.

    // to get the pager set up
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;


    private boolean mLandscape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        mLandscape = isLandscape();

        //get information from intent
        Intent intent = getIntent();

        if (null != savedInstanceState) {
            // if we turned the screen, make sure we are using the same recipe step!
            mRecipeStepPosition = savedInstanceState.getInt(ARG_RECIPE_STEP_POSITION);
        } else if (intent.hasExtra(ARG_RECIPE_STEP_POSITION)) {
            // no saved state, let's check the intent
            mRecipeStepPosition = intent.getIntExtra(ARG_RECIPE_STEP_POSITION, 0);
        } else {
            // *shrug*
            mRecipeStepPosition = 0;
        }


        if (intent.hasExtra(RecipeDetailFragment.ARG_RECIPE)) {
            mRecipe = intent.getParcelableExtra(RecipeDetailFragment.ARG_RECIPE);
        }



        if (mLandscape) {
            // create the fragment and put it into the activity
            RecipeStepDetailFragment recipeStepDetailFragment =
                    RecipeStepDetailFragment.newInstance(mRecipe.getStep(mRecipeStepPosition));
            recipeStepDetailFragment.setViewMode(RecipeStepDetailFragment.ViewMode.VIDEO_ONLY);

            // add the detail fragment, always.
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_step_detail_container,recipeStepDetailFragment)
                    .commit();
        } else {
            // set ourselves up for paging
            mPager = findViewById(R.id.recipe_step_pager);
            mPagerAdapter = new RecipeStepPagerAdapter(getSupportFragmentManager(),
                    mRecipe.getSteps(), RecipeStepDetailFragment.ViewMode.NORMAL );
            mPager.setAdapter(mPagerAdapter);
            mPager.setCurrentItem(mRecipeStepPosition);

            // and for toolbar-ing
            // get the toolbar straightened out
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            ActionBar actionBar = getSupportActionBar();

            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mRecipe.getName());
        }
    }

    // helper methods

    /**
     * determine if we are in landscape mode, by noting we have no toolbar in that mode.
     * @return
     */
    private boolean isLandscape() {
        return findViewById(R.id.toolbar) == null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        // for state saving, let's get the current position in the step array.
        int currentPosition = mRecipeStepPosition;
        if (! isLandscape()) {
            // the pager knows the current position
            currentPosition = mPager.getCurrentItem();
        }
        outState.putInt(ARG_RECIPE_STEP_POSITION, currentPosition); // save the position for future use.
        super.onSaveInstanceState(outState);
    }
}
