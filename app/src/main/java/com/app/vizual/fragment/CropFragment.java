package com.app.vizual.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.vizual.APIResponse.ApiService;
import com.app.vizual.CroppedImageViewActivity;
import com.app.vizual.ImageViewActivity;
import com.app.vizual.Interfaces.FragmentToActivity;
import com.app.vizual.MainActivity;
import com.app.vizual.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class CropFragment extends Fragment {
    private Bitmap bm;
    private String nameImage;
    private CropImageView cropImageView;
    private FloatingActionButton fabCancel, fabCrop, fabOriginalImage;
    private boolean isOverlayShowed = false;
    private FragmentToActivity mCallback;
    private int left = 0, top = 0;

    public CropFragment() {
        // Required empty public constructor
    }

    public CropFragment(Bitmap bitmap, String nameImage) {
        bm = bitmap;
        this.nameImage = nameImage;
    }

    public CropFragment(Bitmap bitmap, String nameImage, int left, int top){
        bm = bitmap;
        this.nameImage = nameImage;
        this.left = left;
        this.top = top;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crop, container, false);
        cropImageView = view.findViewById(R.id.cropImageView);
        fabCancel = view.findViewById(R.id.fabCancel);
        fabCrop = view.findViewById(R.id.fabCrop);
        fabOriginalImage = view.findViewById(R.id.fabOriginalImage);

        mCallback = (FragmentToActivity) getContext();
        cropImageView.setImageBitmap(bm);
        cropImageView.setShowCropOverlay(true);
        isOverlayShowed = true;

        clickFabCrop();
        clickFabCancel();
        clickGetOriginal();
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
                Intent intent = new Intent(getActivity(), CroppedImageViewActivity.class);
                intent.putExtra("nameImage", nameImage);
                intent.putExtra("left", cropImageView.getCropRect().left+ZoomFragment.left);
                intent.putExtra("top", cropImageView.getCropRect().top+ZoomFragment.top+top);
                intent.putExtra("width", cropImageView.getCropRect().width());
                intent.putExtra("height", cropImageView.getCropRect().height());
                intent.putExtra("level", ZoomFragment.level);
                startActivity(intent);
            }
            isOverlayShowed = true;
            cropImageView.setShowCropOverlay(true);
        });
    }

    //Da provare
    private void clickGetOriginal(){
        fabOriginalImage.setOnClickListener(view -> {
            bm = ImageViewActivity.originalBitmap;
            cropImageView.setImageBitmap(bm);
            mCallback.communicate(bm);
        });
    }
}