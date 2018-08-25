package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.data.RecipeStep;

import timber.log.Timber;

public class RecipeStepDetailActivity extends AppCompatActivity
    implements RecipeStepNavigationFragment.RecipeStepNavigationListener{

    private Recipe mRecipe; // with the recipe, we can have the recipe name AND all steps for paging.
    private int mRecipeStepPosition; // the recipe step we're on.

    // to get the pager set up


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

        chooseStep(mRecipeStepPosition);

        if(!mLandscape) {
            // here we need to set up the rest of the UI
            // get the toolbar straightened out

            // the navigation bar goes here.
            RecipeStepNavigationFragment recipeStepNavigationFragment =
                    RecipeStepNavigationFragment.newInstance(mRecipeStepPosition, mRecipe.getSteps().size());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_step_navigation_container, recipeStepNavigationFragment)
                    .commit();


            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            ActionBar actionBar = getSupportActionBar();

            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mRecipe.getName());
        }
    }

    // helper methods

    /**
     * this method will allow the correct fragment to show up.
     * Allows for the navigation buttons to call, as well as initial setup
     * @param stepPosition
     */
    private void chooseStep(int stepPosition) {
        RecipeStepDetailFragment.ViewMode viewMode = (mLandscape) ?
                RecipeStepDetailFragment.ViewMode.VIDEO_ONLY :
                RecipeStepDetailFragment.ViewMode.NORMAL;
            setUpFragment(mRecipe, stepPosition, viewMode);
    }

    /**
     * determine if we are in landscape mode, by noting we have no toolbar in that mode.
     * @return
     */
    private boolean isLandscape() {
        return findViewById(R.id.toolbar) == null;
    }

    /**
     * helper to create fragments given the correct parameters.
     * wraps around the RecipeStepDetailFragment call
     * @param recipe
     * @param stepPosition
     * @param viewMode
     */
    private void setUpFragment(Recipe recipe, int stepPosition,
                               RecipeStepDetailFragment.ViewMode viewMode) {
        RecipeStepDetailFragment.getNewOrCurrent(getSupportFragmentManager(),
                recipe, stepPosition, viewMode);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        // for state saving, let's get the current position in the step array.
        int currentPosition = mRecipeStepPosition;
        outState.putInt(RecipeDetailActivity.ARG_RECIPE_STEP_POSITION, currentPosition); // save the position for future use.
        super.onSaveInstanceState(outState);
    }

    /**
     * handles the case where the left button was clicked
     */
    @Override
    public int onLeftClicked() {
        Timber.i("onLeftClicked() : Position = %d", mRecipeStepPosition);

        if (mRecipeStepPosition > 0) {
            mRecipeStepPosition--; // previous step
            chooseStep(mRecipeStepPosition);
        }
        return mRecipeStepPosition;
    }

    /**
     * and when the right is clicked.
     */
    @Override
    public int onRightClicked() {
        Timber.i("onRightClicked() : Position = %d", mRecipeStepPosition);
        if (mRecipeStepPosition < mRecipe.getSteps().size() - 1) {

            mRecipeStepPosition++; // next step
            chooseStep(mRecipeStepPosition);
        }
        return mRecipeStepPosition;
    }
}
