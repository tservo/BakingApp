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
import com.example.android.bakingapp.RecipeDetailActivity;
import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.database.AppDatabase;
import com.example.android.bakingapp.utilities.JsonUtilities;
import com.example.android.bakingapp.utilities.PreferencesHelper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
    private static RemoteViews getIngredientsListView(Context context, Intent intentRVService) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_recipe_ingredients);

        // we want to make the list here
        views.setRemoteAdapter(R.id.widget_ingredients_listview, intentRVService);
        views.setEmptyView(R.id.widget_ingredients_listview, android.R.id.empty);

        return views;
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                Recipe recipe, int appWidgetId) {



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
        RemoteViews views = getIngredientsListView(context, intentRVService);
        views.setTextViewText(R.id.widget_recipe_name, widgetText);


        // Create an Intent to launch RecipeDetailActivity.
        Intent intent = new Intent(context, RecipeDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);

        // and here's the click handler. put it on the whole widget.
        views.setOnClickPendingIntent(R.id.widget_recipe_name, pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /**
     * put the recipe in from the intent server
     * @param context
     * @param appWidgetManager
     * @param recipe
     * @param appWidgetIds
     */
    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager,
                                      Recipe recipe, int [] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipe, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // start the intent service, which handles updating the ui
        RecipeUpdatingIntentService.startActionUpdateRecipeWidgets(context);
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

