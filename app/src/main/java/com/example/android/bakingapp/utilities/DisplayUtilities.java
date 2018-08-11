package com.example.android.bakingapp.utilities;

import android.content.Context;
import android.util.DisplayMetrics;

public class DisplayUtilities {

    /**
     * This class is a helper class for display functions
     */


        // these are for showing large size images
        private static final float CARD_WIDTH = 400;

        /**
         * utility class to send two floats as a return
         */
        private static class Point {
            final float x;
            final float y;

            Point(float x, float y) {
                this.x = x;
                this.y = y;
            }
        }

        private static Point calculateDp(Context context) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

            float dpHeight = displayMetrics.heightPixels / displayMetrics.density;

            return new Point(dpWidth,dpHeight);
        }

        /**
         * As suggested by my mentor
         * Programmatically determines the best number of columns to create
         * for the Grid Layout. This helps to optimize for landscape mode.
         * @param context Android context
         * @return int number of columns to use
         */
        public static int calculateNoOfColumns(Context context) {

            float dpWidth = calculateDp(context).x;


            int noOfColumns = (int) (dpWidth / CARD_WIDTH);
            if(noOfColumns < 1)
                noOfColumns = 1;


            return noOfColumns;
        }

}
