package com.samkhyatech.photoapp;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SelectedIMageDescription extends DialogFragment {
    private static final String TAG = "DailogSelectDoctors";
    private static final String IMAGE_URL = "image_url";
    @Bind(R.id.imgView)
    ImageView imgView;
    @Bind(R.id.descText)
    EditText descText;
    private String imageUrl;


    public SelectedIMageDescription() {

    }

    public static SelectedIMageDescription newInstance(String imageUrl) {
        SelectedIMageDescription fragment = new SelectedIMageDescription();
        Bundle bundle = new Bundle();
        bundle.putString(IMAGE_URL, imageUrl);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Log.e(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.layout_image_view_desc, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        imageUrl = getArguments().getString(IMAGE_URL);
        File imgFile = null;
        if (imageUrl != null) {
            imgFile = new File(imageUrl);
            if(imgFile.exists()){
                Picasso.with(getActivity()).load(Uri.fromFile(imgFile)).into(imgView);
            }else {
                dismiss();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
