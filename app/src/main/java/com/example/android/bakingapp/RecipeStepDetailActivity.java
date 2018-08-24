package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.android.bakingapp.data.Recipe;

public class RecipeStepDetailActivity extends AppCompatActivity {

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
            mRecipeStepPosition = savedInstanceState.getInt(RecipeDetailActivity.ARG_RECIPE_STEP_POSITION);
        } else if (intent.hasExtra(RecipeDetailActivity.ARG_RECIPE_STEP_POSITION)) {
            // no saved state, let's check the intent
            mRecipeStepPosition = intent.getIntExtra(RecipeDetailActivity.ARG_RECIPE_STEP_POSITION, 0);
        } else {
            // *shrug*
            mRecipeStepPosition = 0;
        }


        if (intent.hasExtra(RecipeDetailFragment.ARG_RECIPE)) {
            mRecipe = intent.getParcelableExtra(RecipeDetailFragment.ARG_RECIPE);
        }



        if (mLandscape) {
            // use this to create the fragment if necessary
            RecipeStepDetailFragment.setupFragment(getSupportFragmentManager(), mRecipe, mRecipeStepPosition,
                    RecipeStepDetailFragment.ViewMode.VIDEO_ONLY, R.id.recipe_step_detail_container);

        } else {
            // set ourselves up for paging
            mPager = findViewById(R.id.recipe_step_pager);
            mPagerAdapter = new RecipeStepPagerAdapter(getSupportFragmentManager(),
                    mRecipe, RecipeStepDetailFragment.ViewMode.NORMAL );
            mPager.setAdapter(mPagerAdapter);
            mPager.setCurrentItem(mRecipeStepPosition);
            mPager.setOffscreenPageLimit(1);

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
        outState.putInt(RecipeDetailActivity.ARG_RECIPE_STEP_POSITION, currentPosition); // save the position for future use.
        super.onSaveInstanceState(outState);
    }
}
