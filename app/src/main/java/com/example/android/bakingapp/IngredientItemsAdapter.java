package com.example.android.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import com.example.android.bakingapp.data.Ingredient;

import java.util.ArrayList;

import timber.log.Timber;


/**
 * handles the list view of the ingredients
 * uses ViewHolder pattern.
 */
public class IngredientItemsAdapter extends ArrayAdapter<Ingredient> {
    private ViewHolder mViewHolder;

    public IngredientItemsAdapter(Context context, ArrayList<Ingredient> ingredients) {
        super(context,R.layout.ingredient_item_view,ingredients);
    }


    @Override
    public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {

        Timber.d("Position %d",position);

        // have we made our view yet?
        if (null == convertView) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.ingredient_item_view, parent, false );
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.mCheckBox.setText(getItem(position).toString());

        return convertView;
    }



    public static class ViewHolder {
        CheckBox mCheckBox;

        ViewHolder(View itemView) {
            mCheckBox = itemView.findViewById(R.id.ingredient_item);
        }

    }
}
