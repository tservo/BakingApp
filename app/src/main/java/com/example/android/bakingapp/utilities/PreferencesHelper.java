package com.example.android.bakingapp.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.data.Recipe;

// here we will store the id of the current recipe -- it's easier than writing a separate table and class to do so.
public class PreferencesHelper {
    public static final long INVALID_RECIPE_ID = -1;

    public static void setCurrentRecipe(Context context, Recipe recipe) {
        if (null == recipe) return; // no point if we have no recipe

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putLong(context.getString(R.string.PREF_CURRENT_RECIPE),recipe.getId());
        editor.apply();
    }

    public static long getCurrentRecipeId(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getLong(context.getString(R.string.PREF_CURRENT_RECIPE),
                INVALID_RECIPE_ID);
    }
}
