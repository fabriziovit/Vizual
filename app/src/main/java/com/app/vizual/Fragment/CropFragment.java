package com.app.vizual.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.vizual.CroppedImageViewActivity;
import com.app.vizual.ImageViewActivity;
import com.app.vizual.Interfaces.FragmentToActivity;
import com.app.vizual.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImageView;

public class CropFragment extends Fragment {
    private Bitmap bm;
    private String nameImage;
    private CropImageView cropImageView;
    private FloatingActionButton fabCancel, fabCrop, fabOriginalImage;
    private boolean isOverlayShowed = false;
    private FragmentToActivity mCallback;
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
                left = left+(int) Math.floor((cropImageView.getCropRect().left+ZoomFragment.left)*ImageViewActivity.ratio);
                top = top+(int) Math.floor((cropImageView.getCropRect().top+ZoomFragment.top)*ImageViewActivity.ratio);
                intent.putExtra("left", left);
                intent.putExtra("top", top);
                intent.putExtra("width",  (int)Math.floor(cropImageView.getCropRect().width()*ImageViewActivity.ratio));//viene preso il width e l'height dell'immagine passata quindi se viene passata
                intent.putExtra("height", (int)Math.floor(cropImageView.getCropRect().height()*ImageViewActivity.ratio));

                double ratioCrop = Math.max((double) cropImageView.getCropRect().width()*ImageViewActivity.ratio/ImageViewActivity.maxWidth,
                        (double) cropImageView.getCropRect().height()*ImageViewActivity.ratio/ ImageViewActivity.maxHeight);

                if(ratioCrop > 1){
                    ImageViewActivity.ratio = ratioCrop;
                }else{
                    if(ImageViewActivity.ratio != 1)
                        ImageViewActivity.ratio = 1;
                }
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