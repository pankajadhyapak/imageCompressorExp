package com.samkhyatech.photoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends MyActivity {

    private static final String TAG = "MainActivity";
    public static final int REQUEST_TAKE_PHOTO = 0;
    public static final int REQUEST_TAKE_VIDEO = 1;
    public static final int REQUEST_PICK_PHOTO = 2;
    public static final int REQUEST_PICK_VIDEO = 3;
    public static final String IMG_STOREAGE_PATH = "/socioDoc";

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.rv_selectedImage)
    RecyclerView rvSelectedImage;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    private Uri mMediaUri;
    public static final int MEDIA_TYPE_IMAGE = 4;
    public static final int MEDIA_TYPE_VIDEO = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        String imgPath = "/storage/emulated/0/Pictures/socioDoc/IMG_20160616_1413161801399438.jpg";
        File imgFile = new File(imgPath);
        Log.e(TAG, "File Size: " + imgFile.length() );

//        ImageView imgView = (ImageView) findViewById(R.id.imgView);
//        String imgPath = "/storage/emulated/0/Pictures/socioDoc/IMG_20160616_1413161801399438.jpg";
//        File imgFile = new File(imgPath);
//        Picasso.with(this).load(Uri.fromFile(imgFile)).into(imgView);

    }

    private void choosePhoto() {
        checkStoragePermission();
        Log.e(TAG, "choosePhoto: ");
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_PICK_PHOTO);
    }

    private void checkStoragePermission() {
    }

    private void takePhoto() {
        checkStoragePermission();
        Log.e(TAG, "takePhoto: ");
        mMediaUri = Utils.getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        if (mMediaUri == null) {
            Toast.makeText(this,
                    "There was a problem accessing your device's external storage.",
                    Toast.LENGTH_LONG).show();
        } else {
            Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
            startActivityForResult(takePhotoIntent, REQUEST_TAKE_PHOTO);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO:
                    Log.e(TAG, "onActivityResult: Take Photo" + mMediaUri.getPath());
                    ImageCompressor.compress(this, mMediaUri.getPath());
                    break;

                case REQUEST_PICK_PHOTO:
                    Log.e(TAG, "onActivityResult: Pick Photo" + data.getData().getPath());
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    String imgDecodableString = "";
                    Cursor cursor = getContentResolver().query(data.getData(), filePathColumn, null, null, null);
                    // Move to first row
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        imgDecodableString = cursor.getString(columnIndex);
                        cursor.close();
                    }
                    Log.e(TAG, "onActivityResult: After COnversion" + imgDecodableString);
                    if (!imgDecodableString.isEmpty()) {
//                        ImageCompressor compressor1 = new ImageCompressor(imgDecodableString, this);
//                        compressor1.compress();
                        ImageCompressor.compress(this, imgDecodableString);
                    }
                    break;
            }
        } else {
            Toast.makeText(MainActivity.this, "Cancelled by User", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.fab)
    public void onClick() {
        Log.e(TAG, "FAB onClick: ");
        final CharSequence[] items = {"Take Photo", "Choose Photo"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Make your selection");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        takePhoto();
                        break;
                    case 1:
                        choosePhoto();
                        break;
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
