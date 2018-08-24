package com.example.android.bakingapp.utilities;

import android.arch.persistence.room.TypeConverter;

import com.example.android.bakingapp.data.Ingredient;
import com.example.android.bakingapp.data.RecipeStep;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import timber.log.Timber;

// this will be used for Room to handle the lists of ingredients and recipe steps
public class ObjectConverter {
    @TypeConverter
    public String ingredientsToJson(ArrayList<Ingredient> ingredients) {
        return JsonUtilities.objectListToJson(ingredients);
    }

    @TypeConverter
    public String recipeStepsToJson(ArrayList<RecipeStep> recipeSteps) {
        return JsonUtilities.objectListToJson(recipeSteps);
    }

    @TypeConverter
    public ArrayList<Ingredient> jsonToIngredients(String json) {
        Type ingredientsArrayListType = new TypeToken<ArrayList<Ingredient>>() {}.getType();
        ArrayList<Ingredient> ingredients = JsonUtilities.jsonToObjectList(json, ingredientsArrayListType);
        Timber.i("Ingredient: %s", ingredients.get(0).toString());
        return ingredients;
    }

    @TypeConverter
    public ArrayList<RecipeStep> jsonToRecipeSteps(String json) {
        Type stepsArrayListType = new TypeToken<ArrayList<RecipeStep>>() {}.getType();

        return JsonUtilities.jsonToObjectList(json,stepsArrayListType);
    }
}
