package com.example.android.bakingapp.utilities;

import android.content.Context;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.data.Recipe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class JsonUtilities {
    public static final String RECIPE_URL = "http://go.udacity.com/android-baking-app-json";

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
     * wrapper method for getting json string from file or from network
     * @param context
     * @return
     */
    private static String retrieveJson(Context context) {
        return retrieveJsonFromFile(context);
    }

    public static ArrayList<Recipe> retrieveRecipes(Context context) {

        String json = retrieveJson(context);
        Gson gson = new GsonBuilder().create();
        Timber.d("Json: %s",json);
        // now slurp up the JSON in an attempt to build the recipe collection.
        Type arrayListType = new TypeToken<ArrayList<Recipe>>() {}.getType();
        return gson.fromJson(json, arrayListType);
    }
}
