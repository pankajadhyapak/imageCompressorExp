package com.samkhyatech.photoapp;

import android.util.Log;

import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class SamplePermissionListener implements PermissionListener {

    private static final String TAG = "PermissionListener";
    private final MainActivity activity;

    public SamplePermissionListener(MainActivity activity) {
        this.activity = activity;
    }

    @Override public void onPermissionGranted(PermissionGrantedResponse response) {

        Log.e(TAG, "onPermissionGranted:"+ response.getPermissionName() );

    }

    @Override public void onPermissionDenied(PermissionDeniedResponse response) {
        Log.e(TAG, "onPermissionDenied:"+ response.getPermissionName() );

    }

    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
        Log.e(TAG, "onPermissionRationaleShouldBeShown:"+ permission.getName() );
        token.continuePermissionRequest();
    }
}
