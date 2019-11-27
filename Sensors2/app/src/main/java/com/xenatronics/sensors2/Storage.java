package com.xenatronics.sensors2;

import android.content.Context;
import android.content.SharedPreferences;

public class Storage {

    public static void saveString(Context context, final String name, final String value) {
        final SharedPreferences prefs = context.getSharedPreferences("com.xenatronics.wemos2_preferences", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(name, value);
        editor.commit();
    }

    public static String getString(Context context, final String name, final String defValue) {
        final SharedPreferences prefs = context.getSharedPreferences("com.xenatronics.wemos2_preferences", Context.MODE_PRIVATE);
        return prefs.getString(name, defValue);
    }

    public static Boolean getBoolean(Context context, final String name, final Boolean defValue) {
        final SharedPreferences prefs = context.getSharedPreferences("com.xenatronics.wemos2_preferences", Context.MODE_PRIVATE);
        return prefs.getBoolean(name, defValue);
    }
}
