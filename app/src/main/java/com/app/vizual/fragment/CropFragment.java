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
                System.out.println("CROP: "+cropImageView.getCropRect().left+" "+cropImageView.getCropRect().top+" "+cropImageView.getCropRect().width()+"  "+cropImageView.getCropRect().height());

                //Richiesta Api passandogli left = cropImageView.getCropRect().left+ visibleRect.left top= cropImageView.getCropRect().top+visibleRect.top
                //width = cropImageView.getCropRect().width()  height = cropImageView.getCropRect().height()
                //Creare nuova Activity/Fragment con il nuovo bitmap.
                //Salvare il bitmap originale e creare un tasto per poterlo poi utilizzare nella imageview.
                /*
                Zoom: 336 0 2617  1488
                CROP: 25 433 1466  893

                left = 336+25  top= 0+433 w : 1466 h:893
                 */
                cropImageView.setImageBitmap(bm);
                mCallback.communicate(bm);
            }
            isOverlayShowed = true;
            cropImageView.setShowCropOverlay(true);
        });
    }
}