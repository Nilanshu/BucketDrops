package com.nilanshu.bucketdrops;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by Nilanshu on 28-Nov-16.
 */
public class AppBucketDrops extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
