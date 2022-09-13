package com.app.vizual.Fragment;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.vizual.Presenters.CropPresenter;
import com.app.vizual.Interfaces.FragmentToActivity;
import com.app.vizual.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImageView;

public class CropFragment extends Fragment {
    public static Bitmap bm;
    private String nameImage;
    private CropImageView cropImageView;
    private FloatingActionButton fabCancel, fabCrop, fabOriginalImage;
    public static boolean isOverlayShowed = false;
    private FragmentToActivity mCallback;
    public static int left = 0, top = 0;
    private CropPresenter cropPresenter;

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
        cropPresenter = new CropPresenter(this);


        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            cropImageView = view.findViewById(R.id.cropImageView);
            fabCancel = view.findViewById(R.id.fabCancel);
            fabCrop = view.findViewById(R.id.fabCrop);
            fabOriginalImage = view.findViewById(R.id.fabOriginalImage);

            mCallback = (FragmentToActivity) getContext();
            cropImageView.setImageBitmap(bm);
            cropImageView.setShowCropOverlay(true);
            isOverlayShowed = true;

            //crop the image passing the coordinates for an api call
            cropPresenter.clickFabCrop(fabCrop, cropImageView, nameImage);
            //set invisible the crop overlay
            cropPresenter.clickFabCancel(fabCancel, cropImageView);
            //Reset Image to the original dimension if it was zoomed or cropped
            cropPresenter.clickGetOriginal(fabOriginalImage, cropImageView);
        }
        return view;
    }
}