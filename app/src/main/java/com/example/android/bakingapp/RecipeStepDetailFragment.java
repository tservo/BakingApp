package com.example.android.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bakingapp.data.RecipeStep;

import timber.log.Timber;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeStepDetailFragment extends Fragment {

    public static enum ViewMode {
        NORMAL, TWO_PANE, VIDEO_ONLY
    }

    public static final String ARG_RECIPE_STEP = "recipe_step";

    private boolean mTwoPane; // are we in a two-pane environment?
    private ViewMode mViewMode = ViewMode.NORMAL; // default to showing all containers

    private RecipeStep mRecipeStep; // our model

    // our views
    // there are three parts to the fragment
    // the media, the step description, and the navigation bar
    // at some points, one or two of these will be hidden.

    private FrameLayout mRecipeStepMediaContainer; // holds the media
    private LinearLayout mRecipeStepDescriptionContainer; // the description
    private FrameLayout mRecipeStepNavigationContainer; // the navigation bar
    private TextView mRecipeDescription;
    private TextView mRecipeDetailDescription;
    private TextView mMediaUrl;

    public RecipeStepDetailFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param recipeStep RecipeStep passed in from parent
     * @return A new instance of fragment RecipeStepDetailFragment.
     */
    public static RecipeStepDetailFragment newInstance(RecipeStep recipeStep) {
        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RECIPE_STEP, recipeStep);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipeStep = getArguments().getParcelable(ARG_RECIPE_STEP);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // link up our views here
        mRecipeStepMediaContainer = view.findViewById(R.id.recipe_step_media_container);
        mRecipeStepDescriptionContainer = view.findViewById(R.id.recipe_step_description_container);
        mRecipeStepNavigationContainer = view.findViewById(R.id.recipe_step_navigation_container);

        mRecipeDescription = mRecipeStepDescriptionContainer.findViewById(R.id.recipe_step_description);
        mRecipeDetailDescription = mRecipeStepDescriptionContainer.findViewById(R.id.recipe_step_detail_description);

        mMediaUrl = view.findViewById(R.id.temp_store_media_url);

        removeUnneededContainers();

        // attempt to put in the longer description into the text box.
        if (null != mRecipeStep) {
            mRecipeDescription.setText(mRecipeStep.getShortDescription());
            mRecipeDetailDescription.setText(mRecipeStep.getDescription());
            populateMediaBox();
        }

    }

    /**
     * Tell the fragment whether it appears in a two pane or single pane layout.
     * @param twoPane
     */
    public void setTwoPane(boolean twoPane) {
        mTwoPane = twoPane;
    }

    public void setViewMode(ViewMode viewMode) {
        mViewMode = viewMode;
    }

    // helper methods down here

    /**
     * handles the code to remove parts of the
     * fragment's view, if they shouldn't be shown.
     */
    private void removeUnneededContainers() {
        // remove the navigation if we have two panes
        switch (mViewMode) {
            case TWO_PANE:
                mRecipeStepNavigationContainer.setVisibility(View.GONE);
                break;
            case VIDEO_ONLY:
                mRecipeStepNavigationContainer.setVisibility(View.GONE);
                mRecipeStepDescriptionContainer.setVisibility(View.GONE);
        }
    }


    /**
     * set up our media box here
     */
    private void populateMediaBox() {
        String videoMedia = mRecipeStep.getVideoURL();
        String stillMedia = mRecipeStep.getThumbnailURL();

        if (null != videoMedia && !videoMedia.equals("")) {
            mMediaUrl.setText("Video: " + videoMedia);
        } else if (null != stillMedia && !stillMedia.equals("")) {
            mMediaUrl.setText("Still: " + stillMedia);
        } else {
            mMediaUrl.setText("No Media");
        }
    }
}
