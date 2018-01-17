package edu.depaul.csc472.gamescreen.utility;

/**
 * Created by keertika on 11/17/2017.
 */

import android.content.Context;
import android.util.TypedValue;

public class Pixel {

    public static int pixelsToDp(int px, Context context) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, px,
                context.getResources().getDisplayMetrics());
    }

}