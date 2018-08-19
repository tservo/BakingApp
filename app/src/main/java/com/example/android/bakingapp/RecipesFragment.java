package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.utilities.DisplayUtilities;
import com.example.android.bakingapp.utilities.JsonUtilities;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipesFragment extends Fragment
    implements RecipeItemsAdapter.RecipeItemsClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_RECIPES = "recipes";

    private ArrayList<Recipe> mRecipes;

    private RecyclerView mRecipesRecyclerView;
    private RecipeItemsAdapter mRecipeItemsAdapter;


    public RecipesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onRecipeItemClick(Recipe recipe) {
        // let's fire an intent to get to the details activity
        Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
        // in here, we have to pass the movie over.
        intent.putExtra(RecipeDetailFragment.ARG_RECIPE, recipe);
        startActivity(intent);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param recipes ArrayList of recipes passed in from parent
     * @return A new instance of fragment RecipesFragment.
     */
    public static RecipesFragment newInstance(ArrayList<Recipe> recipes) {
        RecipesFragment fragment = new RecipesFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_RECIPES, recipes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipes = getArguments().getParcelableArrayList(ARG_RECIPES);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipes, container, false);
        setupRecipesRecyclerView(rootView);

        return rootView;
    }


    /**
     * helper method to set up the recycler view for this fragment
     */
    private void setupRecipesRecyclerView(View rootView) {
        Context context = getContext();
        mRecipesRecyclerView = rootView.findViewById(R.id.recipe_card_recyclerview);

        // let's figure out what layout manager we need
        int numColumns = DisplayUtilities.calculateNoOfColumns(context);
        RecyclerView.LayoutManager layoutManger;

        // 1 column, use linear layout
        if (numColumns <= 1) {
            layoutManger = new LinearLayoutManager(context);
        } else {
            // need multiple columns, use grid layout manager!
            layoutManger = new GridLayoutManager(context, numColumns);
        }
        mRecipesRecyclerView.setLayoutManager(layoutManger);

        // now the adapter
        mRecipeItemsAdapter = new RecipeItemsAdapter(context, mRecipes, this );
        mRecipesRecyclerView.setAdapter(mRecipeItemsAdapter);
    }
}
