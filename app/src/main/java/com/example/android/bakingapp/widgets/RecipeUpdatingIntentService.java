package com.example.android.bakingapp.widgets;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.database.AppDatabase;
import com.example.android.bakingapp.utilities.PreferencesHelper;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 *
 * Using this service to get the recipe from the database, so that it won't run in the UI thread.
 */
public class RecipeUpdatingIntentService extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_UPDATE_RECIPE_WIDGETS = "com.example.android.bakingapp.widgets.action.update_recipe_widgets";

//    private static final String EXTRA_PARAM1 = "com.example.android.bakingapp.widgets.extra.PARAM1";
//    private static final String EXTRA_PARAM2 = "com.example.android.bakingapp.widgets.extra.PARAM2";

    public RecipeUpdatingIntentService() {
        super("RecipeUpdatingIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionUpdateRecipeWidgets(Context context) {
        Intent intent = new Intent(context, RecipeUpdatingIntentService.class);
        intent.setAction(ACTION_UPDATE_RECIPE_WIDGETS);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_RECIPE_WIDGETS.equals(action)) {
                handleActionUpdateRecipeWidgets();
            }
        }
    }

    /**
     * gets the recipe from the database and in the hands of the widget provider.
     */
    private void handleActionUpdateRecipeWidgets() {
        // get the recipe in question.
        AppDatabase database = AppDatabase.getInstance(getApplicationContext());
        long currentId = PreferencesHelper.getCurrentRecipeId(getApplicationContext());
        Recipe recipe = database.RecipeDao().getRecipe(currentId);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeIngredientsWidgetProvider.class));
        //Now update all widgets

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_ingredients_listview);
        RecipeIngredientsWidgetProvider.updateRecipeWidgets(this, appWidgetManager, recipe, appWidgetIds);
    }

}
