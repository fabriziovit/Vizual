package com.app.vizual.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.vizual.Interfaces.FragmentToActivity;
import com.app.vizual.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImageView;

public class CropFragment extends Fragment {
    View view;
    Bitmap bm;
    CropImageView cropImageView;
    FloatingActionButton fabCancel, fabCrop;
    boolean isOverlayShowed = false;
    private FragmentToActivity mCallback;


    public CropFragment() {
        // Required empty public constructor
    }

    public CropFragment(Bitmap bitmap) {
        bm = bitmap;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_crop, container, false);
        cropImageView = view.findViewById(R.id.cropImageView);
        fabCancel = view.findViewById(R.id.fabCancel);
        fabCrop = view.findViewById(R.id.fabCrop);

        mCallback = (FragmentToActivity) getContext();
        cropImageView.setImageBitmap(bm);
        cropImageView.setShowCropOverlay(true);
        isOverlayShowed = true;

        clickFabCrop();
        clickFabCancel();
        return view;
    }


    private void clickFabCancel(){
       fabCancel.setOnClickListener(view -> {
           isOverlayShowed = false;
           cropImageView.setShowCropOverlay(false);

        });
    }

    private void clickFabCrop(){
        fabCrop.setOnClickListener(view -> {
            if(isOverlayShowed) {
                cropImageView.setShowCropOverlay(false);
                isOverlayShowed = false;
                bm = cropImageView.getCroppedImage();
                cropImageView.setImageBitmap(bm);
                mCallback.communicate(bm);
            }
            isOverlayShowed = true;
            cropImageView.setShowCropOverlay(true);
        });
    }
}