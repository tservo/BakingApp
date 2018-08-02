package com.example.android.bakingapp;

import android.content.Context;
import android.content.SearchRecentSuggestionsProvider;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.data.Recipe;

import java.util.ArrayList;

/**
 * This class owes a lot to https://medium.com/@ramtrg/https-medium-com-ramtrg-mvvm-architecture-components-4d17d3f09bb7
 * From which we will use the MVVM pattern.
 */
public class RecipeItemsAdapter extends RecyclerView.Adapter<RecipeItemsAdapter.RecipesListViewHolder> {

    private Context mContext; // holds context for later use
    private ArrayList<Recipe> mRecipeList; // holds the recipe list that is displayed

    /**
     *
     */
    public RecipeItemsAdapter(Context context, ArrayList<Recipe> recipeArrayList) {
        mContext = context;
        mRecipeList = recipeArrayList;
    }

    /**
     *
     * @param parent parent view group
     * @param viewType -- not used at this point
     * @return new view holder
     */
    @NonNull
    @Override
    public RecipesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        // get the card view that the view holder holds.
        View view = layoutInflater.inflate(R.layout.recipe_card_view, parent, false);

        return new RecipesListViewHolder(view);
    }

    /**
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull RecipesListViewHolder holder, int position) {
        Recipe recipe = mRecipeList.get(position);

        holder.recipeTitle.setText(recipe.getName());
        holder.recipeSummary.setText("Number of servings " + String.valueOf(recipe.getServings()));
    }

    /**
     *
     * @return the number of items in the stored list
     */
    @Override
    public int getItemCount() {
        if (null == mRecipeList) return 0; // guard clause
        return mRecipeList.size();
    }

    // View Holder
    public class RecipesListViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

        final TextView recipeTitle;
        final ImageView recipeImage;
        final TextView recipeSummary;

        RecipesListViewHolder(View itemView) {
            super(itemView);

            // set up the fields.  maybe go to data binding
            recipeTitle = itemView.findViewById(R.id.recipe_title);
            recipeImage = itemView.findViewById(R.id.recipe_image);
            recipeSummary = itemView.findViewById(R.id.recipe_summary);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
