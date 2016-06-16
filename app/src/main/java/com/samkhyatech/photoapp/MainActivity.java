package com.samkhyatech.photoapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends MyActivity implements ImageDescRvAdapter.OnAdapterInteractionListener {

    private static final String TAG = "MainActivity";
    private static final String CASE_IMG_ = "case_img_";
    private static final String CASE_DESC_ = "case_desc_";
    public static final int REQUEST_TAKE_PHOTO = 0;
    public static final int REQUEST_TAKE_VIDEO = 1;
    public static final int REQUEST_PICK_PHOTO = 2;
    public static final int REQUEST_PICK_VIDEO = 3;
    public static final String IMG_STOREAGE_PATH = "/socioDoc";

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.rv_selectedImages)
    RecyclerView rvSelectedImages;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    private Uri mMediaUri;
    public static final int MEDIA_TYPE_IMAGE = 4;
    public static final int MEDIA_TYPE_VIDEO = 5;
    SharedPreferences sharedPref;
    ArrayList<SelectedImage> selectedImages = new ArrayList<>(3);
    private ImageDescRvAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        sharedPref = getPreferences(Context.MODE_PRIVATE);
        chekIntentFromDescription();
        rvSelectedImages.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ImageDescRvAdapter(selectedImages, this);
        rvSelectedImages.setAdapter(mAdapter);
        populateList();

    }

    private void populateList() {

        Map<String,?> keys = sharedPref.getAll();
        for(Map.Entry<String,?> entry : keys.entrySet()){
            if(entry.getKey().startsWith(CASE_IMG_)){
                String[] temp = entry.getKey().split("_");
                String imgKey = entry.getKey();
                String descKey = CASE_DESC_+temp[temp.length -1];
                Log.e(TAG, "populateList: "+descKey );
                SelectedImage imgSel = new SelectedImage();
                imgSel.setSelectedImg(sharedPref.getString(imgKey,""));
                imgSel.setDescription(sharedPref.getString(descKey,""));
                imgSel.setKey(temp[temp.length -1]);
                Log.e(TAG, "Present Images : " + entry.getValue());
                selectedImages.add(imgSel);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private void chekIntentFromDescription() {
        Intent intent = getIntent();
        String imgPath = intent.getStringExtra(AddDescriptionActivity.IMAGE_FILE_PATH);
        String desc = intent.getStringExtra(AddDescriptionActivity.IMAGE_DESC);

        if(!TextUtils.isEmpty(imgPath) && !TextUtils.isEmpty(desc)){
            addImageToCase(imgPath, desc);
        }
    }

    private void choosePhoto() {
        Log.e(TAG, "choosePhoto: ");
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_PICK_PHOTO);
    }


    private void takePhoto() {
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
                    String compressedFilePath = ImageCompressor.compress(this, mMediaUri.getPath());
                    if(!compressedFilePath.isEmpty()){
                        Intent intent = new Intent(this, AddDescriptionActivity.class);
                        intent.putExtra("imageFilePath", compressedFilePath);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                    }
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
                        compressedFilePath = ImageCompressor.compress(this, imgDecodableString);
                        if(!compressedFilePath.isEmpty()){
                            Intent intent = new Intent(this, AddDescriptionActivity.class);
                            intent.putExtra("imageFilePath", compressedFilePath);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                        }
                    }
                    break;
            }
        } else {
            Toast.makeText(MainActivity.this, "Cancelled by User", Toast.LENGTH_SHORT).show();
        }
    }

    private void addImageToCase(String compressedFilePath, String description) {
        Log.e(TAG, "addImageToCase: " + compressedFilePath );
        String appendable = System.currentTimeMillis() + "";

        sharedPref.edit()
                .putString(CASE_IMG_+appendable, compressedFilePath)
                .putString(CASE_DESC_+appendable, description).apply();

        Map<String,?> keys = sharedPref.getAll();

        for(Map.Entry<String,?> entry : keys.entrySet()){
            if(entry.getKey().startsWith(CASE_IMG_)){
                Log.e(TAG, "Present Images : " + entry.getValue());
            }
            if(entry.getKey().startsWith(CASE_DESC_)){
                Log.e(TAG, "Present Desc : " + entry.getValue());
            }
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
//        Intent intent = new Intent(this, AddDescriptionActivity.class);
//        intent.putExtra("imageFilePath", "/storage/emulated/0/Pictures/socioDoc/COMP_pankaj2.jpg");
//        startActivity(intent);
//        overridePendingTransition(0,0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_remove:
                Map<String, ?> keys = sharedPref.getAll();
                for (Map.Entry<String, ?> entry : keys.entrySet()) {
                    if (entry.getKey().startsWith(CASE_IMG_) || entry.getKey().startsWith(CASE_DESC_)) {
                        sharedPref.edit().remove(entry.getKey()).apply();
                    }
                }
                break;
            case R.id.action_display:
                Map<String,?> keys1 = sharedPref.getAll();
                for(Map.Entry<String,?> entry : keys1.entrySet()){
                    if(entry.getKey().startsWith(CASE_IMG_)){
                        Log.e(TAG, "Present Images : " + entry.getValue());
                        Log.e(TAG, "Present Images Key: " + entry.getKey());
                    }
                    if(entry.getKey().startsWith(CASE_DESC_)){
                        Log.e(TAG, "Present Desc : " + entry.getValue());
                        Log.e(TAG, "Present Desc key: " + entry.getKey());
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDeleteBtnClicked(int position) {
        sharedPref.edit()
                .remove(CASE_DESC_+selectedImages.get(position).getKey())
                .remove(CASE_IMG_+selectedImages.get(position).getKey()).apply();
        selectedImages.remove(selectedImages.get(position));
        mAdapter.notifyItemRemoved(position);

    }
}
