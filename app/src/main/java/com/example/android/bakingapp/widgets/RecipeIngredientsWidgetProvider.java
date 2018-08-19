package com.example.android.bakingapp.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.example.android.bakingapp.MainActivity;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.utilities.JsonUtilities;

import static com.example.android.bakingapp.RecipeDetailFragment.ARG_RECIPE;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeIngredientsWidgetProvider extends AppWidgetProvider {

    public static final String ARG_RECIPE_BUNDLE = "bundle";

    /**
     * helper to handle the ingredients list.
     * @param context the context of the app
     * @return
     */
    private static RemoteViews getIngredientsListView(Context context, Recipe recipe, Intent intentRVService) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_recipe_ingredients);

        // we want to make the list here
        views.setRemoteAdapter(R.id.widget_ingredients_listview, intentRVService);
        views.setEmptyView(R.id.widget_ingredients_listview, android.R.id.empty);

        return views;
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // get the recipe in question.
        Recipe recipe = JsonUtilities.retrieveTestRecipe(context);

        // we need the widget's name
        CharSequence widgetText = (recipe == null) ?
                context.getString(R.string.NO_RECIPE) :
                recipe.getName();

        // the intent to update the remote listview
        Intent intentRVService = new Intent(context,RecipeIngredientsWidgetService.class);
        intentRVService.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        // this seems to be necessary to get the recipe sent over to the remoteviewsfactory
        // https://stackoverflow.com/questions/28589509/android-e-parcel-class-not-found-when-unmarshalling-only-on-samsung-tab3
        Bundle b = new Bundle();
        b.putParcelable(ARG_RECIPE, recipe);

        intentRVService.putExtra(ARG_RECIPE_BUNDLE, b);
        intentRVService.setData(Uri.parse(intentRVService.toUri(Intent.URI_INTENT_SCHEME)));

        // Construct the RemoteViews object
        RemoteViews views = getIngredientsListView(context, recipe, intentRVService);
        views.setTextViewText(R.id.widget_recipe_name, widgetText);


        // Create an Intent to launch MainActivity when clicked
        // we actually want RecipeDetailActivity for the recipe in question
        // but that requires the recipe to be passed along
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);

        // and here's the click handler
        views.setOnClickPendingIntent(R.id.widget_recipe_name, pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        // and tell the listview that its data has changed.
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_ingredients_listview);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

