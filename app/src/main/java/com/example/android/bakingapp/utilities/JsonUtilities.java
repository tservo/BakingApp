package com.example.android.bakingapp.utilities;

import android.arch.persistence.room.TypeConverter;
import android.content.Context;

import com.example.android.bakingapp.IdlingResource.SimpleIdlingResource;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.data.Recipe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import timber.log.Timber;

public class JsonUtilities {
    public static interface RecipeWebService {
        @GET("android-baking-app-json")
        Call<ArrayList<Recipe>> getRecipes();
    }

    public static final String RECIPE_URL = "http://go.udacity.com/";

    private static Random sRandomGenerator = new Random(); // for testing the widget.
    private static RecipeWebService sRecipeWebService;

    static {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RECIPE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        sRecipeWebService = retrofit.create(RecipeWebService.class);
    }
    /**
     * https://stackoverflow.com/questions/6349759/using-json-file-in-android-app-resources
     * @param context
     * @return
     */
    private static String retrieveJsonFromFile(Context context) {
        InputStream resourceReader = context.getResources().openRawResource(R.raw.baking);
        Writer writer = new StringWriter();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(resourceReader, "UTF-8" ));
            String line = reader.readLine();
            while (line != null) {
                writer.write(line);
                line = reader.readLine();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                resourceReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return writer.toString();
    }

    /**
     * retrieve json from network
     * @return the json
     */
    private static String retrieveJsonFromNetwork() {
        return null; // TODO: implement network loading
    }

    /**
     * helper method to return the recipes from json already gotten from source
     * @param json the json
     * @return ArrayList of Recipes
     */
    private static ArrayList<Recipe> retrieveRecipesFromJson(String json) {
        Gson gson = new GsonBuilder().create();
        Timber.d("Json: %s",json);
        // now slurp up the JSON in an attempt to build the recipe collection.
        Type arrayListType = new TypeToken<ArrayList<Recipe>>() {}.getType();
        return gson.fromJson(json, arrayListType);
    }

    // Serializing lists of objects for the database
    // so we don't need so many tables! Ingredients and steps don't make sense outside
    // of a recipe context.
    // https://medium.com/@toddcookevt/android-room-storing-lists-of-objects-766cca57e3f9

    /**
     * turn a list of objects into json
     * @param objectList the list of objects, probably ingredients or steps
     * @param <T> Ingredient | RecipeStep
     * @return the json.
     */
    public static <T> String objectListToJson(List<T> objectList) {
        Gson gson = new GsonBuilder().create();

        String json = gson.toJson(objectList);
        Timber.d("Our Json: %s",json);
        return json;

    }

    public static <T> ArrayList<T> jsonToObjectList(String json, Type listType) {
        Gson gson = new GsonBuilder().create();

        if (null == json) return null;

        return gson.fromJson(json, listType);
    }


    /**
     * from JSON, let's get our recipes from the web.
     * @return ArrayList of Recipes
     */
    public static ArrayList<Recipe> retrieveRecipes() {
        Call<ArrayList<Recipe>> call = sRecipeWebService.getRecipes();
        Response<ArrayList<Recipe>> response;


        try {
            response = call.execute();
        } catch (IOException e) {
            Timber.w("Network Error: %s", e.getMessage());
            return null;
        }
        if (response.isSuccessful()) {
            return response.body();
        } else {
            Timber.w("Response Error: %s",response.errorBody());
            return null;
        }
    }

    /**
     * interface to retrieve recipes from file, passing in a context.
     * @param context used to retrieve JSON from file. It's not necessary for retrieving from web.
     * @return ArrayList of Recipes
     */
    public static ArrayList<Recipe> retrieveRecipes(Context context) {
        String json = retrieveJsonFromFile(context);
        return retrieveRecipesFromJson(json);
    }

    /**
     * test method - get a single recipe from file
     * @param context used to retrieve JSON from file.
     * @return a single Recipe.
     */
    public static Recipe retrieveTestRecipe(Context context) {
        ArrayList<Recipe> recipes = retrieveRecipes(context);

        if (null == recipes || recipes.size() == 0) {
            return null; // oops, no recipes to return
        }

        return recipes.get(sRandomGenerator.nextInt(recipes.size()));
    }

}
