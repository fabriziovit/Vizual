package com.app.vizual.Fragment;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.vizual.Interfaces.FragmentToActivity;
import com.app.vizual.Presenters.CropPresenter;
import com.app.vizual.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImageView;

public class CropFragment extends Fragment {
    public static Bitmap bm;
    private String nameImage;
    public static boolean isOverlayShowed = false;
    public static int left = 0, top = 0;

    public CropFragment() {
        // Required empty public constructor
    }

    public CropFragment(Bitmap bitmap, String nameImage) {
        bm = bitmap;
        this.nameImage = nameImage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crop, container, false);
        CropImageView cropImageView = view.findViewById(R.id.cropImageView);
        FloatingActionButton fabCancel = view.findViewById(R.id.fabCancel);
        FloatingActionButton fabCrop = view.findViewById(R.id.fabCrop);
        FloatingActionButton fabOriginalImage = view.findViewById(R.id.fabOriginalImage);
        CropPresenter cropPresenter = new CropPresenter(this);

        FragmentToActivity mCallback = (FragmentToActivity) getContext();
        cropImageView.setImageBitmap(bm);
        cropImageView.setShowCropOverlay(true);
        isOverlayShowed = true;

        cropPresenter.clickFabCancel(fabCancel, cropImageView);
        cropPresenter.clickFabCrop(fabCrop, cropImageView, nameImage);
        cropPresenter.clickGetOriginal(fabOriginalImage, cropImageView);
        return view;
    }
}