package com.example.android.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.bakingapp.data.Ingredient;
import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.data.RecipeStep;

import java.util.ArrayList;

import timber.log.Timber;


/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeDetailFragment extends Fragment {

    private Recipe mRecipe;

    // holds a reference to the parent activity as a click listener
    // the idea is from the Android Me lesson.
    private RecipeStepItemsAdapter.RecipeStepItemsClickListener mCallback;

    private ListView mLvIngredients;
    private RecyclerView mRvRecipeSteps;

    public static final String ARG_RECIPE = "recipe";

    public RecipeDetailFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param recipe Recipe passed in from parent
     * @return A new instance of fragment RecipeDetailFragment.
     */
    public static RecipeDetailFragment newInstance(Recipe recipe) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RECIPE, recipe);
        fragment.setArguments(args);
        return fragment;
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (RecipeStepItemsAdapter.RecipeStepItemsClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement RecipeStepItemsAdapter.RecipeStepItemsClickListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // from here we need to unparcel the recipe passed in
        if (getArguments() != null) {
            mRecipe = getArguments().getParcelable(ARG_RECIPE);
        } else {
            mRecipe = null;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_recipe_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLvIngredients = view.findViewById(R.id.ingredients_list_view);
        mRvRecipeSteps = view.findViewById(R.id.recipe_step_recyclerview);

        populateRecipeView();
    }

    /**
     * handles filling up the recipe view as necessary
     */
    private void populateRecipeView() {
        if (mRecipe == null) {
            Timber.w("Where's my recipe?");
            return;
        }

        // create the list adapter and populate the listview
        IngredientItemsAdapter ingredientAdapter = new IngredientItemsAdapter(getContext(), mRecipe.getIngredients());
        mLvIngredients.setAdapter(ingredientAdapter);

        // and the steps recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecipeStepItemsAdapter recipeStepItemsAdapter = new RecipeStepItemsAdapter(
                getContext(), mRecipe.getSteps(), mCallback);
        mRvRecipeSteps.setLayoutManager(layoutManager);
        mRvRecipeSteps.setAdapter(recipeStepItemsAdapter);

    }

}

