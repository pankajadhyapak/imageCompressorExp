package com.samkhyatech.photoapp;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pankaj on 16/06/16.
 */
public class Utils {

    private static final String TAG = "Utils";

    public static Uri getOutputMediaFileUri(int mediaType) {
        String fileName = "";
        String fileType = "";

        // check for external storage
        if (isExternalStorageAvailable()) {
            // 1. Get the external storage directory
            File AppStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath() + MainActivity.IMG_STOREAGE_PATH);
            if (!AppStorageDir.exists()) {
                AppStorageDir.mkdirs();
            }
            Log.e(TAG, "getOutputMediaFileUri: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath());

            // 2. Create a unique file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            if (mediaType == MainActivity.MEDIA_TYPE_IMAGE) {
                fileName = "IMG_" + timeStamp;
                fileType = ".jpg";
            } else if (mediaType == MainActivity.MEDIA_TYPE_VIDEO) {
                fileName = "VID_" + timeStamp;
                fileType = ".mp4";
            } else {
                return null;
            }

            // 3. Create the file
            File mediaFile;
            try {
                mediaFile = File.createTempFile(fileName, fileType, AppStorageDir);
                Log.i(TAG, "File: " + Uri.fromFile(mediaFile));

                // 4. Return the file's URI
                return Uri.fromFile(mediaFile);
            } catch (IOException e) {
                Log.e(TAG, "Error creating file: ", e);
            }
        }

        // something went wrong
        return null;
    }

    public static boolean isExternalStorageAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }


}
