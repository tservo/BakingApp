package com.example.android.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import timber.log.Timber;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeStepNavigationFragment.RecipeStepNavigationListener} interface
 * to handle interaction events.
 * Use the {@link RecipeStepNavigationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeStepNavigationFragment extends Fragment {

    public interface RecipeStepNavigationListener {
        int onLeftClicked();
        int onRightClicked();
    }

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_INITIAL_STEP = "starting_position";
    private static final String ARG_NUM_STEPS = "num_steps";

    private int mCurrentPosition;
    private int mNumSteps;

    private Button mLeftButton;
    private Button mRightButton;


    private RecipeStepNavigationListener mListener;

    public RecipeStepNavigationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param initialPosition the starting position for the recipe steps
     * @param numSteps the number of steps the recipe has
     * @return A new instance of fragment RecipeStepNavigationFragment.
     */
    public static RecipeStepNavigationFragment newInstance(int initialPosition, int numSteps) {
        RecipeStepNavigationFragment fragment = new RecipeStepNavigationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INITIAL_STEP, initialPosition);
        args.putInt(ARG_NUM_STEPS, numSteps);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           mCurrentPosition = getArguments().getInt(ARG_INITIAL_STEP);
            mNumSteps = getArguments().getInt(ARG_NUM_STEPS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_step_navigation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLeftButton = view.findViewById(R.id.button_left);
        mLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.i("Left onClick()");
                mCurrentPosition = mListener.onLeftClicked();
                disableButtons();
            }
        });
        mRightButton = view.findViewById(R.id.button_right);
        mRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.i("Right onClick()");

                mCurrentPosition = mListener.onRightClicked();
                disableButtons();
            }
        });

        disableButtons();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecipeStepNavigationListener) {
            mListener = (RecipeStepNavigationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void disableButtons() {
        mLeftButton.setEnabled(mCurrentPosition > 0);
        mRightButton.setEnabled(mCurrentPosition < mNumSteps - 1);
    }
}
