package com.example.distanceconverter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    private SharedPreferences shared_pref;

    SharedPref(Activity activity) {
        shared_pref = activity.getSharedPreferences(activity.getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
    }

    void saveInt(String key, int value) {
        SharedPreferences.Editor editor = shared_pref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    int getInt(String key) {
        return shared_pref.getInt(key, 0);
    }
}
