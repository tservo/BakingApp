package com.example.android.bakingapp.data;

public class Ingredient {
    public double quantity;
    public String measure; // maybe redo this field in deserialization?
    public String ingredient;

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

}
