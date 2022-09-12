package com.app.vizual.Presenters;

import static com.app.vizual.Fragment.CropFragment.bm;

import android.content.Intent;

import com.app.vizual.Views.CroppedImageViewActivity;
import com.app.vizual.Fragment.CropFragment;
import com.app.vizual.Fragment.ZoomFragment;
import com.app.vizual.Views.ImageViewActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImageView;

public class CropPresenter {
    private final CropFragment cropFragment;
    public static double ratioCrop;

    public CropPresenter(CropFragment cropFragment) {
        this.cropFragment = cropFragment;
    }

    public void clickFabCancel(FloatingActionButton fabCancel, CropImageView cropImageView){
        fabCancel.setOnClickListener(view -> {
            CropFragment.isOverlayShowed = false;
            cropImageView.setShowCropOverlay(false);
        });
    }

    public void clickFabCrop(FloatingActionButton fabCrop, CropImageView cropImageView, String nameImage){
        fabCrop.setOnClickListener(view -> {
            if(CropFragment.isOverlayShowed) {
                cropImageView.setShowCropOverlay(false);
                CropFragment.isOverlayShowed = false;
                Intent intent = new Intent(cropFragment.getActivity(), CroppedImageViewActivity.class);
                intent.putExtra("nameImage", nameImage);
                CropFragment.left =  CropFragment.left+(int) Math.floor((cropImageView.getCropRect().left+ ZoomFragment.left)* ImageViewActivity.ratio);
                CropFragment.top =  CropFragment.top+(int) Math.floor((cropImageView.getCropRect().top+ZoomFragment.top)*ImageViewActivity.ratio);
                intent.putExtra("left",  CropFragment.left);
                intent.putExtra("top",  CropFragment.top);
                intent.putExtra("width",  (int)Math.floor(cropImageView.getCropRect().width()*ImageViewActivity.ratio));//viene preso il width e l'height dell'immagine passata quindi se viene passata
                intent.putExtra("height", (int)Math.floor(cropImageView.getCropRect().height()*ImageViewActivity.ratio));

                ratioCrop = Math.max((double) cropImageView.getCropRect().width()*ImageViewActivity.ratio/ImageViewActivity.maxWidth,
                        (double) cropImageView.getCropRect().height()*ImageViewActivity.ratio/ ImageViewActivity.maxHeight);

                if(ratioCrop > 1){
                    ImageViewActivity.ratio = ratioCrop;
                }else{
                    if(ImageViewActivity.ratio != 1)
                        ImageViewActivity.ratio = 1;
                }
                cropFragment.startActivity(intent);
            }
            CropFragment.isOverlayShowed = true;
            cropImageView.setShowCropOverlay(true);
        });
    }

    public void clickGetOriginal(FloatingActionButton fabOriginalImage, CropImageView cropImageView){
        fabOriginalImage.setOnClickListener(view -> {
            bm = ImageViewActivity.originalBitmap;
            CropFragment.left = 0;
            CropFragment.top = 0;
            ImageViewActivity.ratio = ImageViewActivity.originalRatio;
            cropImageView.setImageBitmap(bm);
            ImageViewActivity.bm = ImageViewActivity.originalBitmap;
            ZoomFragment.bm = ImageViewActivity.originalBitmap;
            CroppedImageViewActivity.bmp = ImageViewActivity.originalBitmap;
        });
    }
}
