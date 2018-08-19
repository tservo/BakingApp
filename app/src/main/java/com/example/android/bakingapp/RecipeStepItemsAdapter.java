package com.example.android.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
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
    final private RecipeStepItemsClickListener mClickListener;

    public interface RecipeStepItemsClickListener {
        void onRecipeStepItemClick(RecipeStep recipeStep, int clickPosition);
    }
    /**
     *
     * @param context the app context
     * @param recipeSteps the list of recipe steps to show in the list
     */
    public RecipeStepItemsAdapter(Context context, ArrayList<RecipeStep> recipeSteps,
                                  RecipeStepItemsClickListener clickListener) {
        mContext = context;
        mRecipeSteps = recipeSteps;
        mClickListener = clickListener;
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

        if (null != step) {
           holder.stepDescription.setText(step.getShortDescription());
        }
    }

    @Override
    public int getItemCount() {
        if (null == mRecipeSteps) return 0;
        return mRecipeSteps.size();
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

            // make sure we have a valid position
            Timber.d("Position: %d",clickPosition);
            if (clickPosition == RecyclerView.NO_POSITION || clickPosition > mRecipeSteps.size()) {
                recipeStep = null;
            } else {
                recipeStep = mRecipeSteps.get(clickPosition);
            }

            // now fire the listener!
            mClickListener.onRecipeStepItemClick(recipeStep, clickPosition);
        }
    }
}
