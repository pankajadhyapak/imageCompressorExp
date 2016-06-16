package com.samkhyatech.photoapp;

import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener;

/**
 * Created by pankaj on 16/06/16.
 */
public class MyActivity extends AppCompatActivity {

    private static final int PERMISSION_GRANTED = 1;
    private static final int PERMISSION_Denied = 2;
    private static final int PERMISSION_REJECTED = 3;
    private int currentPermission = 3;

    private PermissionListener PermissionListener;


    public int checkPermission(String permissionName){

        ViewGroup rootView = (ViewGroup) findViewById(android.R.id.content);

        PermissionListener = new CompositePermissionListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                currentPermission = PERMISSION_GRANTED;
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                currentPermission = PERMISSION_Denied;

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
                currentPermission = PERMISSION_REJECTED;
            }
        },
                SnackbarOnDeniedPermissionListener.Builder.with(rootView,
                        R.string.contacts_permission_denied_feedback)
                        .withOpenSettingsButton(R.string.permission_rationale_settings_button_text).withOpenSettingsButton("Setting")
                        .build());

        if (! Dexter.isRequestOngoing()) {

            Dexter.checkPermission(
                    PermissionListener,
                    permissionName
            );

        }

        return PERMISSION_REJECTED;

    }
}
