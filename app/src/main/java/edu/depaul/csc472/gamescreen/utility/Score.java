package edu.depaul.csc472.gamescreen.utility;

/**
 * Created by keertika on 11/20/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;

public class Score {

    private static final String GLOBAL = "global";
    private static final String CURRENT_SCORE = "current_score";
    private static final String CURRENT_LEVEL = "current_level";
    //private static final String MOST_LEVELS = "most_levels";

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(
                GLOBAL, Context.MODE_PRIVATE);
    }

    //  Setters and getters for global preferences
    public static void setCurrentScore(Context context, int score) {
        SharedPreferences.Editor editor =
                getPreferences(context).edit();
        editor.putInt(CURRENT_SCORE, score);
        editor.apply();
    }

    public static void setCurrentLevel(Context context, int level) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putInt(CURRENT_LEVEL, level);
        editor.apply();
    }


}
