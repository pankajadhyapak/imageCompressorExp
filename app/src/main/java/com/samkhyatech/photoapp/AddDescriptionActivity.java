package com.samkhyatech.photoapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddDescriptionActivity extends AppCompatActivity {

    public static final String IMAGE_FILE_PATH = "imageFilePath";
    public static final String IMAGE_DESC = "imageDesc";
    @Bind(R.id.imgView)
    ImageView imgView;

    @Bind(R.id.descText)
    EditText descText;
    @Bind(R.id.saveBtn)
    Button saveBtn;
    @Bind(R.id.cancelBtn)
    Button cancelBtn;
    String imgPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_description);
        ButterKnife.bind(this);
        imgPath = getIntent().getStringExtra("imageFilePath");
        File imgFile = null;
        if (imgPath != null) {
            imgFile = new File(imgPath);
            if (imgFile.exists()) {
                Picasso.with(this).load(Uri.fromFile(imgFile)).into(imgView);
            } else {
                finish();
                overridePendingTransition(0, 0);
            }
        }
    }

    @OnClick({R.id.saveBtn, R.id.cancelBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveBtn:
                if(! descText.getText().toString().isEmpty()) {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra(IMAGE_FILE_PATH, imgPath);
                    intent.putExtra(IMAGE_DESC, descText.getText().toString());
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }else {
                    Toast.makeText(AddDescriptionActivity.this, "Please Add Description", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cancelBtn:
                finish();
                overridePendingTransition(0, 0);
                break;
        }
    }
}
