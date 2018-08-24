package com.example.android.bakingapp;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.data.RecipeStep;

import java.util.ArrayList;

import timber.log.Timber;

public class RecipeStepPagerAdapter extends FragmentStatePagerAdapter {
    private Recipe mRecipe;
    private RecipeStepDetailFragment.ViewMode mViewMode;

    /**
     * constructor to get list of recipe steps ready for paging
     * @param fm fragment manager
     * @param recipe the parent recipe as the model
     * @param viewMode the view mode of the fragment - normal, two_pane, or video_only
     */
    public RecipeStepPagerAdapter(FragmentManager fm, Recipe recipe,
                                  RecipeStepDetailFragment.ViewMode viewMode) {
        super(fm);
        mRecipe = recipe;
        mViewMode = viewMode;
    }
    @Override
    public Fragment getItem(int position) {
        RecipeStep recipeStep;
        Long recipeId;

        if (null == mRecipe) {
            Timber.w("No recipe!");
            recipeStep = null;
            recipeId = null;
        } else {
           recipeStep = mRecipe.getStep(position);
            recipeId = mRecipe.getId();
           if (null == recipeStep) {
               Timber.w("Unable to get the step: recipe id: %l, position: %d", recipeId, position);
           }
        }

        Timber.i("Calling newInstance: %d",position);
        RecipeStepDetailFragment fragment = RecipeStepDetailFragment.newInstance(recipeStep, position, mRecipe.getId());
        fragment.setViewMode(mViewMode);
        return fragment;
    }

    @Override
    public int getCount() {
        if (null == mRecipe) return 0;

        if (null == mRecipe.getSteps()) return 0;

        return mRecipe.getSteps().size();
    }
}
