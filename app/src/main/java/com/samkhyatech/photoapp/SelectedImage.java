package com.samkhyatech.photoapp;

/**
 * Created by pankaj on 16/06/16.
 */
public class SelectedImage {

    String selectedImg;
    String description;
    String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "SelectedImage{" +
                "selectedImg='" + selectedImg + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public String getSelectedImg() {
        return selectedImg;
    }

    public void setSelectedImg(String selectedImg) {
        this.selectedImg = selectedImg;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
