package com.example.triviaapp.util;

import android.app.Activity;
import android.content.SharedPreferences;

public class Prefs {
    SharedPreferences sharedPreferences;

    public Prefs(Activity activity) {
        this.sharedPreferences = activity.getPreferences(activity.MODE_PRIVATE);
    }
    public void saveHighScore(int score){
        int current_score=score;
        int lastScore=sharedPreferences.getInt("high_score",0);
        if(current_score>lastScore){
            sharedPreferences.edit().putInt("high_score",current_score).apply();
        }
    }
    public int getHighScore(){
        return sharedPreferences.getInt("high_score",0);
    }

}
