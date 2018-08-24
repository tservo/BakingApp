package com.example.android.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.data.RecipeStep;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import timber.log.Timber;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeStepDetailFragment extends Fragment {

    public static enum ViewMode {
        NORMAL, TWO_PANE, VIDEO_ONLY
    }

    // the keys to store player state.
    public static final String PLAYER_LAST_POSITION = "player_last_position";
    public static final String PLAYER_LAST_CURRENT_WINDOW = "player_last_current_window";
    public static final String PLAYER_PLAY_WHEN_READY = "player_play_when_ready";

    private boolean mTwoPane; // are we in a two-pane environment?
    private ViewMode mViewMode = ViewMode.NORMAL; // default to showing all containers


    private RecipeStep mRecipeStep; // our model
    private int mRecipeStepPosition; // what is our step position?
    private long mRecipeId; // what is our recipe?

    // our views
    // there are three parts to the fragment
    // the media, the step description, and the navigation bar
    // at some points, one or two of these will be hidden.

    private FrameLayout mRecipeStepMediaContainer; // holds the media
    private PlayerView mPlayerView; // the exoplayer
    private ImageView mStillImageView; // the still image

    private LinearLayout mRecipeStepDescriptionContainer; // the description
    private FrameLayout mRecipeStepNavigationContainer; // the navigation bar
    private TextView mRecipeDescription;
    private TextView mRecipeDetailDescription;
    private TextView mMediaUrl;

    private SimpleExoPlayer mExoPlayer;
    // these flags as suggested by Slack channel.
    private long mPlaybackPosition = 0L; // storage for exoplayer
    private int mCurrentWindow = 0; // current window index
    private boolean mPlayWhenReady = true; //

    public RecipeStepDetailFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param recipeStep RecipeStep passed in from parent
     * @return A new instance of fragment RecipeStepDetailFragment.
     */
    public static RecipeStepDetailFragment newInstance(RecipeStep recipeStep, int stepPosition, long recipeId) {
        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(RecipeDetailActivity.ARG_RECIPE_STEP, recipeStep);
        args.putInt(RecipeDetailActivity.ARG_RECIPE_STEP_POSITION, stepPosition);
        args.putLong(RecipeDetailActivity.ARG_RECIPE_ID, recipeId);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * method that sets up the fragment and reuses it if it already exists
     * @param fragmentManager passed in from the activity
     * @param recipe
     * @param recipeStepPosition
     */
    public static void setupFragment(FragmentManager fragmentManager, Recipe recipe, int recipeStepPosition,
                                     ViewMode viewMode, int resourceId) {

        Long recipeId = recipe.getId();
        RecipeStepDetailFragment recipeStepDetailFragment;

        // Trying to reuse fragments is proving to be messy, so turning off.

            //    (RecipeStepDetailFragment) fragmentManager.findFragmentById(resourceId);

//        if (null == recipeStepDetailFragment ||
//                recipeStepDetailFragment.getRecipeId() != recipeId ||
//                recipeStepDetailFragment.getRecipeStepPosition() != recipeStepPosition ) {
           //  we need to create a fragment
            recipeStepDetailFragment =
                    RecipeStepDetailFragment.newInstance(recipe.getStep(recipeStepPosition),
                            recipeStepPosition, recipeId);
            recipeStepDetailFragment.setViewMode(viewMode);

            // add the detail fragment, always.
            fragmentManager.beginTransaction()
                    .replace(resourceId,recipeStepDetailFragment)
                    .commit();
//        } else {
//            // we have our fragment, tell it to clean up a bit;
//            Timber.i("We're reusing this fragment with view mode: %s",viewMode.toString());
//            recipeStepDetailFragment.setViewMode(viewMode);
//        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipeStep = getArguments().getParcelable(RecipeDetailActivity.ARG_RECIPE_STEP);
            mRecipeStepPosition = getArguments().getInt(RecipeDetailActivity.ARG_RECIPE_STEP_POSITION);
            mRecipeId = getArguments().getLong(RecipeDetailActivity.ARG_RECIPE_ID);
        }
        if (null != savedInstanceState) {
            mPlaybackPosition = savedInstanceState.getLong(PLAYER_LAST_POSITION, 0L);
            mPlayWhenReady = savedInstanceState.getBoolean(PLAYER_PLAY_WHEN_READY, true);
            mCurrentWindow = savedInstanceState.getInt(PLAYER_LAST_CURRENT_WINDOW, 0);
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

        Timber.i("Fragment %s :OnViewCreated()", this.toString());

        // link up our views here
        mRecipeStepMediaContainer = view.findViewById(R.id.recipe_step_media_container);
        mPlayerView = view.findViewById(R.id.recipe_step_video_container);
        mStillImageView = view.findViewById(R.id.recipe_step_image_container);

        mRecipeStepDescriptionContainer = view.findViewById(R.id.recipe_step_description_container);
        mRecipeStepNavigationContainer = view.findViewById(R.id.recipe_step_navigation_container);

        mRecipeDescription = mRecipeStepDescriptionContainer.findViewById(R.id.recipe_step_description);
        mRecipeDetailDescription = mRecipeStepDescriptionContainer.findViewById(R.id.recipe_step_detail_description);

        mMediaUrl = view.findViewById(R.id.temp_store_media_url);

        removeUnneededContainers();


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // https://stackoverflow.com/questions/46730249/how-to-create-one-fragment-at-a-time-using-viewpager
        super.setUserVisibleHint(isVisibleToUser);
        Timber.i("%s: Called isvisibletouser: %s",this.toString(),String.valueOf(isVisibleToUser));
        Timber.i("%s: this.isVisible() : %s",this.toString(), String.valueOf(this.isVisible()));
            if (mRecipeStep != null) {
                Timber.i("%s setUserVisibleHint: RecipeStep exists",mRecipeStep);
            }

    }

    /**
     * Tell the fragment whether it appears in a two pane or single pane layout.
     * @param twoPane
     */
    public void setTwoPane(boolean twoPane) {
        mTwoPane = twoPane;
        if (null != mRecipeStepDescriptionContainer) {
            removeUnneededContainers();
        }

    }

    public void setViewMode(ViewMode viewMode) {
        mViewMode = viewMode;
        // if we've already started we need to alter the landscape
    }

    /**
     * to tell the parent activity if we are the right recipe step.
     * @return
     */
    public int getRecipeStepPosition() {
        return mRecipeStepPosition;
    }

    /**
     * let the parent activity know if we have the right recipe
     * @return
     */
    public long getRecipeId() {
        return mRecipeId;
    }

    // helper methods down here

    /**
     * handles the code to remove parts of the
     * fragment's view, if they shouldn't be shown.
     */
    private void removeUnneededContainers() {
        Timber.i("Remove unneeded containers: %s",mViewMode.toString());
        switch (mViewMode) {
            case TWO_PANE:
                // remove the navigation if we have two panes
                mRecipeStepDescriptionContainer.setVisibility(View.VISIBLE);
                mRecipeStepNavigationContainer.setVisibility(View.GONE);
                break;
            case VIDEO_ONLY:
                // leave only the video
                mRecipeStepNavigationContainer.setVisibility(View.GONE);
                mRecipeStepDescriptionContainer.setVisibility(View.GONE);
                break;
            default:
                // show it all
                mRecipeStepNavigationContainer.setVisibility(View.VISIBLE);
                mRecipeStepDescriptionContainer.setVisibility(View.VISIBLE);
        }
    }

    // https://google.github.io/ExoPlayer/guide.html

    /**
     * Initialize ExoPlayer.
     * From AdvancedAndroid Classical Music Quiz
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri, int lastCurrentWindow, long playbackPosition, boolean playWhenReady) {
        Timber.i("%s: initializing player. Recipe step: %d",this.toString(), mRecipeStepPosition);
        if (mExoPlayer == null) {
            Context context = getActivity();

            // Create an default TrackSelector
            Handler mainHandler = new Handler();
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
           // mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(context, "BakingApp");
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                    userAgent, null);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(mediaUri);

            mExoPlayer.prepare(mediaSource);
            mExoPlayer.seekTo(lastCurrentWindow, playbackPosition);
            mExoPlayer.setPlayWhenReady(playWhenReady);
        }
    }


    /**
     * set up our media box here
     */
    private void populateMediaBox() {

        String videoMedia = mRecipeStep.getVideoURL();
        String stillMedia = mRecipeStep.getThumbnailURL();

        if (null != videoMedia && !videoMedia.equals("")) {
            //mMediaUrl.setText("Video: " + videoMedia);
            mMediaUrl.setVisibility(View.INVISIBLE);
            mStillImageView.setVisibility(View.INVISIBLE);
            mPlayerView.setVisibility(View.VISIBLE);
            initializePlayer(Uri.parse(videoMedia), mCurrentWindow, mPlaybackPosition, mPlayWhenReady);

        } else if (null != stillMedia && !stillMedia.equals("")) {
            mMediaUrl.setVisibility(View.INVISIBLE);
            mStillImageView.setVisibility(View.VISIBLE);
            mPlayerView.setVisibility(View.INVISIBLE);

            Picasso.get()
                    .load(stillMedia)
                    .error(R.drawable.ic_photo_black_24dp)
                    .into(mStillImageView);
        } else {
            // no media.
            mMediaUrl.setVisibility(View.INVISIBLE);
            mStillImageView.setVisibility(View.VISIBLE);
            mPlayerView.setVisibility(View.INVISIBLE);
            mStillImageView.setImageResource(R.drawable.ic_photo_black_24dp);
        }
    }

    private void cleanupPlayer() {
        if (null != mExoPlayer) {
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    // state changes


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(PLAYER_PLAY_WHEN_READY,mPlayWhenReady);
        outState.putInt(PLAYER_LAST_CURRENT_WINDOW,mCurrentWindow);
        outState.putLong(PLAYER_LAST_POSITION,mPlaybackPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();

        Timber.i("---onPause()");

        if (null != mExoPlayer) {
            Timber.w("mExoPlayer is alive");


            mPlayWhenReady = mExoPlayer.getPlayWhenReady();
            mCurrentWindow = mExoPlayer.getCurrentWindowIndex();
            mPlaybackPosition = mExoPlayer.getCurrentPosition();
            mExoPlayer.setPlayWhenReady(false);
            mExoPlayer.stop();

            if (Build.VERSION.SDK_INT < 24) {
                // onStop won't be called so clean up player now
                cleanupPlayer();
            }
        } else {
            Timber.w("mExoPlayer is dead");
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.i("%s Step %d ---onResume()", this.toString(), mRecipeStepPosition);
        // attempt to put in the longer description into the text box.
        if (null != mRecipeStep) {
            Timber.i("null != mRecipeStep");
            mRecipeDescription.setText(mRecipeStep.getShortDescription());
            mRecipeDetailDescription.setText(mRecipeStep.getDescription());
            populateMediaBox();
        } else {
            Timber.w("null == mRecipeStep!");
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT >= 24) {
            // this will be called, clean up the player now
            cleanupPlayer();
        }
    }

}
