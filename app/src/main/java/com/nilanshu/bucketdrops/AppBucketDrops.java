package com.nilanshu.bucketdrops;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
