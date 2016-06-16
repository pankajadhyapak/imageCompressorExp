package com.samkhyatech.photoapp;

import java.io.File;

/**
 * Created by pankaj on 16/06/16.
 */
public class SelectedImage {

    File selectedImg;
    String description;

    public File getSelectedImg() {
        return selectedImg;
    }

    public void setSelectedImg(File selectedImg) {
        this.selectedImg = selectedImg;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
