package com.example.android.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;

/**
 * stores a single ingredient for a recipe. Recipes will likely have more than one of these.
 * parceled by http://www.parcelabler.com/
 */
public class Ingredient implements Parcelable {
    private double quantity;
    private String measure; // maybe redo this field in deserialization?
    private String ingredient;

    /**
     * helper method to get the measure into a display format
     */
    private String formatMeasure() {
        switch(measure) {
            case "UNIT":
                return "";
            case "G":
                return "g";
            case "TBLSP":
                return "Tbsp";
            case "TSP":
                return "tsp";
            case "CUP":
                if (quantity > 1.0) {
                    return "cups";
                }
                return "cup";
            default:
                return measure;
        }
    }

    /**
     * helper method to return quantity in display format
     * @return formatted quantity.
     */
    private String formatQuantity() {
        DecimalFormat format = new DecimalFormat("###.#");
        return format.format(quantity);

    }

    // for GSON
    public Ingredient() { }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    /**
     * gives a readable display for the ingredient
     */
    public String toString() {
        String units = formatMeasure();
        String qty = formatQuantity();

        // quick and dirty formatting
        String output = (qty.equals("")) ?  "" : qty + " ";
        output += (units.equals("")) ? "" : units + " ";
        output += ingredient;

        return output;
    }


    protected Ingredient(Parcel in) {
        quantity = in.readDouble();
        measure = in.readString();
        ingredient = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}