package com.example.android.bakingapp.utilities;

import android.arch.persistence.room.TypeConverter;

import com.example.android.bakingapp.data.Ingredient;
import com.example.android.bakingapp.data.RecipeStep;

import java.util.ArrayList;

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
        return JsonUtilities.jsonToObjectList(json);
    }

    @TypeConverter
    public ArrayList<RecipeStep> jsonToRecipeSteps(String json) {
        return JsonUtilities.jsonToObjectList(json);
    }
}
