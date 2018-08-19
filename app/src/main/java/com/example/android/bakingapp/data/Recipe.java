package com.example.android.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;


import java.util.ArrayList;

/**
 * Represents a recipe.
 * Parceled by http://www.parcelabler.com/
 */
public class Recipe implements Parcelable {

    private long id;
    private String name;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<RecipeStep> steps;
    private int servings;
    private String image;

    // default constructor for gson
    Recipe() { }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }


    public ArrayList<RecipeStep> getSteps() {
        return steps;
    }

    /**
     * get the ordered recipe step, if it exists
     * @param step the recipe step at this position
     * @return the RecipeStep requested, or null if unable to do so.
     */
    public RecipeStep getStep(int step) {
        // guard if steps are null or we don't have a valid step number
        if (step < 0 || null == steps || steps.size() <= step) return null;

        return steps.get(step);
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    protected Recipe(Parcel in) {
        id = in.readLong();
        name = in.readString();
        if (in.readByte() == 0x01) {
            ingredients = new ArrayList<Ingredient>();
            in.readList(ingredients, Ingredient.class.getClassLoader());
        } else {
            ingredients = null;
        }
        if (in.readByte() == 0x01) {
            steps = new ArrayList<RecipeStep>();
            in.readList(steps, RecipeStep.class.getClassLoader());
        } else {
            steps = null;
        }
        servings = in.readInt();
        image = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        if (ingredients == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(ingredients);
        }
        if (steps == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(steps);
        }
        dest.writeInt(servings);
        dest.writeString(image);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}