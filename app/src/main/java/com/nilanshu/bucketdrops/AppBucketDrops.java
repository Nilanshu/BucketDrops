package com.nilanshu.bucketdrops;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.nilanshu.bucketdrops.adapters.Filter;

import io.realm.Realm;

/**
 * Created by Nilanshu on 28-Nov-16.
 */
public class AppBucketDrops extends Application {

    public static void save(Context context, int filterOption) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("filter", filterOption);
        editor.apply();
    }

    public static int load(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int filterOption = pref.getInt("filter", Filter.NONE);
        return filterOption;
    }

    public static void setRalewayRegular(Context context, TextView textView) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/raleway_thin.ttf");
        textView.setTypeface(typeface);
    }

    public static void setRalewayRegular(Context context, TextView... textViews) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/raleway_thin.ttf");
        for (TextView textView : textViews) {
            textView.setTypeface(typeface);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
