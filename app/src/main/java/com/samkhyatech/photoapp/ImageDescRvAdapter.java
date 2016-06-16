package com.samkhyatech.photoapp;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ImageDescRvAdapter extends RecyclerView.Adapter<ImageDescRvAdapter.DescViewHolder> {


    private static final String TAG = "ImageDescRvAdapter";
    private ArrayList<SelectedImage> selectedImages = new ArrayList<>();
    private Context mContext;
    private OnAdapterInteractionListener mListener;

    public ImageDescRvAdapter(ArrayList<SelectedImage> selectedImages, Context mContext) {
        this.selectedImages = selectedImages;
        this.mContext = mContext;
        mListener = (OnAdapterInteractionListener) mContext;
    }

    @Override
    public DescViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_img_desc, parent, false);

        return new DescViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DescViewHolder holder, int position) {
        SelectedImage imgDesc = selectedImages.get(position);
        File imgFile = null;
        imgFile = new File(imgDesc.getSelectedImg());
        if(imgFile.exists()){
            Picasso.with(mContext).load(Uri.fromFile(imgFile)).into(holder.imgView);
        }
        holder.descText.setText(imgDesc.getDescription());

    }

    @Override
    public int getItemCount() {
        return selectedImages.size();
    }

    public class DescViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.imgView)
        ImageView imgView;
        @Bind(R.id.descText)
        TextView descText;
        @Bind(R.id.deleteImg)
        Button deleteBtn;

        public DescViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onDeleteBtnClicked(getAdapterPosition());
                }
            });
        }

    }

    public interface OnAdapterInteractionListener {
        void onDeleteBtnClicked(int position);
    }
}
