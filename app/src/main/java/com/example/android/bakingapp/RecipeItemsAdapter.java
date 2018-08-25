package com.example.android.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.data.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * This class owes a lot to https://medium.com/@ramtrg/https-medium-com-ramtrg-mvvm-architecture-components-4d17d3f09bb7
 * From which we will use the MVVM pattern.
 */
public class RecipeItemsAdapter extends RecyclerView.Adapter<RecipeItemsAdapter.RecipesListViewHolder> {

    private Context mContext; // holds context for later use
    private ArrayList<Recipe> mRecipeList; // holds the recipe list that is displayed
    final private RecipeItemsClickListener mRecipeItemsClickListener;

    public interface RecipeItemsClickListener {
        void onRecipeItemClick(Recipe recipe);
    }

    /**
     *
     */
    public RecipeItemsAdapter(Context context, ArrayList<Recipe> recipeArrayList, RecipeItemsClickListener clickListener) {
        mContext = context;
        mRecipeList = recipeArrayList;
        mRecipeItemsClickListener = clickListener;
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
        holder.recipeSummary.setText(mContext.getString(R.string.SERVINGS, recipe.getServings()));

        String imageUri = recipe.getImage();
        if (null != imageUri && !(imageUri.equals(""))) {
            // attempt to get the image for the recipe
            Picasso.get()
                    .load(recipe.getImage())
                    .error(R.drawable.ic_photo_black_24dp)
                    .into(holder.recipeImage);
        } else {
            // isn't going to work =(
            holder.recipeImage.setImageResource(R.drawable.ic_photo_black_24dp);
        }
;

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

    /**
     * change the recipe list through this method.
     * @param recipeList
     */
    public void setRecipes(ArrayList<Recipe> recipeList) {

        if (null != mRecipeList && null != recipeList) {
            // we have two ArrayList<Recipe> - merge them
            mRecipeList.clear();
            mRecipeList.addAll(recipeList);
        } else {
            // either set the member variable to null
            // or set the member variable to the list.
            mRecipeList = recipeList;
        }

        notifyDataSetChanged();

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
            itemView.setOnClickListener(this);
        }

        /**
         * Handle click listener on the view
         * @param v not used
         */
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            Recipe recipe;

            // try to get the location of the recipe in the recycler view
            Timber.d("Position %d", clickedPosition);
            if (clickedPosition == RecyclerView.NO_POSITION || clickedPosition >= mRecipeList.size()) {
                recipe = null;
            } else {
                recipe = mRecipeList.get(clickedPosition);
            }
            // and fire the click!
            mRecipeItemsClickListener.onRecipeItemClick(recipe);
        }
    }
}
