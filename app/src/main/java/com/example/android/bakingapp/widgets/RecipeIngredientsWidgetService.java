package com.example.android.bakingapp.widgets;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.data.Ingredient;
import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.database.AppDatabase;
import com.example.android.bakingapp.utilities.PreferencesHelper;

import java.util.ArrayList;

import timber.log.Timber;

import static com.example.android.bakingapp.RecipeDetailFragment.ARG_RECIPE;
import static com.example.android.bakingapp.widgets.RecipeIngredientsWidgetProvider.ARG_RECIPE_BUNDLE;

public class RecipeIngredientsWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        // need an extra step to get the recipe over here.
        Timber.i("onGetViewFactory");
        Bundle b = intent.getBundleExtra(ARG_RECIPE_BUNDLE);
        b.setClassLoader(Recipe.class.getClassLoader());
        Recipe recipe = b.getParcelable(ARG_RECIPE);

        String toString = (recipe == null) ? "null" : recipe.toString();
        Timber.i("Recipe: %s", toString);

        return new RecipeIngredientsRemoteViewsFactory(this.getApplicationContext(), recipe);
    }
}

// the broadcast receiver idea comes from
// https://stackoverflow.com/questions/7367257/passing-an-array-of-integers-from-an-appwidget-to-a-preexisting-remoteviewsservi
class RecipeIngredientsRemoteViewsFactory
        implements RemoteViewsService.RemoteViewsFactory {


    private Context mContext;
    private Recipe mRecipe;
    private ArrayList<Ingredient> mIngredients;

    RecipeIngredientsRemoteViewsFactory(Context context, Recipe recipe) {
        mContext = context;
        mRecipe = recipe;

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

        AppDatabase database = AppDatabase.getInstance(mContext);
        long currentId = PreferencesHelper.getCurrentRecipeId(mContext);
        mRecipe = database.RecipeDao().getRecipe(currentId);

        // now get the ingredients for listing.
        if (null == mRecipe) {
            mIngredients = null;
        } else {
            mIngredients = mRecipe.getIngredients();
        }
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        if (null == mIngredients) return 0;

        return mIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        // guard if the position is invalid
        if (null == mIngredients || getCount() <= position) return null;

        Timber.i("mIngredients: %s", mIngredients.toString());
        Ingredient ingredient = mIngredients.get(position);

        // and here we put the ingredient into the item
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_ingredient_item_view);
        views.setTextViewText(R.id.widget_ingredient_name, ingredient.toString());

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // one type of view -- textview
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

}
