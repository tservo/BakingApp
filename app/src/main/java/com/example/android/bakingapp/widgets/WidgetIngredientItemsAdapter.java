package com.example.android.bakingapp.widgets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.data.Ingredient;

import java.util.ArrayList;

import timber.log.Timber;


/**
 * handles the list view of the ingredients of the widget
 * uses ViewHolder pattern.
 */
public class WidgetIngredientItemsAdapter extends ArrayAdapter<Ingredient> {
    private ViewHolder mViewHolder;



    WidgetIngredientItemsAdapter(Context context, ArrayList<Ingredient> ingredients) {
        super(context,R.layout.widget_ingredient_item_view,ingredients);

    }


    @Override
    public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {

        Timber.d("Position %d",position);

        // have we made our view yet?
        if (null == convertView) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.widget_ingredient_item_view, parent, false );
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.mIngredientView.setText(getItem(position).toString());

        return convertView;
    }



    public static class ViewHolder {
        TextView mIngredientView;

        ViewHolder(View itemView) {
            mIngredientView = itemView.findViewById(R.id.ingredient_item);
        }

    }
}
