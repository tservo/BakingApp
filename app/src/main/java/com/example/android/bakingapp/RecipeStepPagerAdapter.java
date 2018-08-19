package com.example.android.bakingapp;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.android.bakingapp.data.RecipeStep;

import java.util.ArrayList;

import timber.log.Timber;

public class RecipeStepPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<RecipeStep> mRecipeSteps;
    private RecipeStepDetailFragment.ViewMode mViewMode = RecipeStepDetailFragment.ViewMode.NORMAL;

    /**
     * constructor to get list of recipe steps ready for paging
     * @param fm fragment manager
     * @param recipeSteps list of recipe steps as the model
     * @param viewMode the view mode of the fragment - normal, two_pane, or video_only
     */
    public RecipeStepPagerAdapter(FragmentManager fm, ArrayList<RecipeStep> recipeSteps,
                                  RecipeStepDetailFragment.ViewMode viewMode) {
        super(fm);
        mRecipeSteps = recipeSteps;
        mViewMode = viewMode;
    }
    @Override
    public Fragment getItem(int position) {
        RecipeStep recipeStep;
        if (position < 0 || null == mRecipeSteps || mRecipeSteps.size() <= position) {
            Timber.w("Invalid position: %d", position);
            recipeStep = null;
        } else {
            recipeStep = mRecipeSteps.get(position);
        }
        RecipeStepDetailFragment fragment = RecipeStepDetailFragment.newInstance(recipeStep);
        fragment.setViewMode(mViewMode);
        return fragment;
    }

    @Override
    public int getCount() {
        if (null == mRecipeSteps) return 0;

        return mRecipeSteps.size();
    }
}
