package com.samkhyatech.photoapp;

import android.app.Application;

import com.karumi.dexter.Dexter;

/**
 * Created by pankaj on 16/06/16.
 */
public class PhotoApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Dexter.initialize(this);
    }
}
