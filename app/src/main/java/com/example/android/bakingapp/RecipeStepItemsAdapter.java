package com.example.android.bakingapp;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.data.RecipeStep;

import java.util.ArrayList;

import timber.log.Timber;

public class RecipeStepItemsAdapter extends RecyclerView.Adapter<RecipeStepItemsAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<RecipeStep> mRecipeSteps;
    private boolean mHighlightSelections;

    final private RecipeStepItemsClickListener mClickListener;


    // https://stackoverflow.com/questions/27194044/how-to-properly-highlight-selected-item-on-recyclerview
    private int mFocusedItem;

    public interface RecipeStepItemsClickListener {
        void onRecipeStepItemClick(RecipeStep recipeStep, int clickPosition);
    }
    /**
     *
     * @param context the app context
     * @param recipeSteps the list of recipe steps to show in the list
     */
    public RecipeStepItemsAdapter(Context context, ArrayList<RecipeStep> recipeSteps,
                                  RecipeStepItemsClickListener clickListener,
                                  boolean highlightSelections,
                                  int initialRecipeStepPosition) {
        mContext = context;
        mRecipeSteps = recipeSteps;
        mClickListener = clickListener;
        mHighlightSelections = highlightSelections;
        mFocusedItem = initialRecipeStepPosition;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        View view = layoutInflater.inflate(R.layout.recipe_step_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecipeStep step = mRecipeSteps.get(position);
        if (mHighlightSelections) {
            handleHighlight(holder, (position == mFocusedItem));
        }

        if (null != step) {
           holder.stepDescription.setText(step.getShortDescription());
        }
    }

    /**
     * helper method to handle the highlight of the selected item.
     * @param holder the viewholder in question
     * @param isSelected whether or not it should be highlighted
     */
    private void handleHighlight(@NonNull ViewHolder holder, boolean isSelected) {
        int lightGray = ContextCompat.getColor(mContext, R.color.light_gray);

        if (isSelected) {
            holder.itemView.setBackgroundColor(Color.BLACK);
            holder.stepDescription.setTextColor(lightGray);
        } else {
            holder.itemView.setBackgroundColor(lightGray);
            holder.stepDescription.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        if (null == mRecipeSteps) return 0;
        return mRecipeSteps.size();
    }

    public void setRecipeSteps(ArrayList<RecipeStep> recipeSteps) {
        // guard against null steps

        if (null == recipeSteps || recipeSteps.size() == 0) return;
        mRecipeSteps.clear();
        mRecipeSteps.addAll(recipeSteps);
        notifyDataSetChanged();
    }

    /**
     * ViewHolder for the recyclerview.
     */
    public class ViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

        final TextView stepDescription;

        ViewHolder(View itemView) {
            super(itemView);

            stepDescription = itemView.findViewById(R.id.recipe_step_description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickPosition = getAdapterPosition();
            RecipeStep recipeStep;
            mFocusedItem = clickPosition;

            // make sure we have a valid position
            Timber.d("Position: %d",clickPosition);
            if (clickPosition == RecyclerView.NO_POSITION || clickPosition > mRecipeSteps.size()) {
                recipeStep = null;
            } else {
                recipeStep = mRecipeSteps.get(clickPosition);
            }
            notifyDataSetChanged(); // get the highlight to update!

            // now fire the listener!
            mClickListener.onRecipeStepItemClick(recipeStep, clickPosition);
        }
    }
}
