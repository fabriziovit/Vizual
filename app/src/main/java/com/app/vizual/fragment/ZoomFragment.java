package com.app.vizual.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.vizual.CustomGlideApp;
import com.app.vizual.ImageViewActivity;
import com.app.vizual.Interfaces.FragmentToActivity;
import com.app.vizual.R;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ZoomFragment extends Fragment{
    private Bitmap bm;
    private SubsamplingScaleImageView subsamplingScaleImageView;
    private FloatingActionButton fabPassImage, fabZoom15, fabOriginalImage;
    private FragmentToActivity mCallback;
    public static int left = 0, top = 0;

    public ZoomFragment() {
        // Required empty public constructor
    }

    public ZoomFragment(Bitmap bitmap) {
        bm = bitmap;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zoom, container, false);
        mCallback = (FragmentToActivity) getContext();

        subsamplingScaleImageView = view.findViewById(R.id.zoomImageView);
        CustomGlideApp glideApp = new CustomGlideApp();
        glideApp.init(this, bm, subsamplingScaleImageView);
        fabPassImage = view.findViewById(R.id.fabPassImageZoomed);
        fabZoom15 = view.findViewById(R.id.fabZoom15);
        fabOriginalImage = view.findViewById(R.id.fabOriginalImage);

        clickReset();
        clickGetImageResized();

        //clickZoom15();

        return view;
    }

    //get Image zoomed to the crop
    private void clickGetImageResized() {
        fabPassImage.setOnClickListener(view -> {
            PointF leftTopCoord = subsamplingScaleImageView.viewToSourceCoord(new PointF(0, 0));
            PointF rightBottomCoord = subsamplingScaleImageView.viewToSourceCoord(new PointF(subsamplingScaleImageView.getWidth(), subsamplingScaleImageView.getHeight()));
            RectF visibleRectF = new RectF(leftTopCoord.x, leftTopCoord.y, rightBottomCoord.x, rightBottomCoord.y);
            System.out.println(visibleRectF);
            final Rect visibleRect = new Rect();
            visibleRectF.round(visibleRect);
            System.out.println("Zoom: "+visibleRect.left+" "+visibleRect.top+" "+visibleRect.width()+"  "+visibleRect.height());
            left = visibleRect.left;
            top = visibleRect.top;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();
            ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
            BitmapRegionDecoder decoder = null;
            try {
                decoder = BitmapRegionDecoder.newInstance(bs, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap visibleWallBitmap = decoder.decodeRegion(visibleRect, null);

            subsamplingScaleImageView.setImage(ImageSource.bitmap(visibleWallBitmap));
            mCallback.communicate(visibleWallBitmap);
        });
    }

    //Da Fixare
    //Reset zoom and position in the image
    private void clickReset() {
        //Richiesta immagine 2.5x
        fabOriginalImage.setOnClickListener(view -> {
            //Errore Bitmap:recycle() vedere come risolvere
            bm.recycle();
            subsamplingScaleImageView.setImage(ImageSource.bitmap(ImageViewActivity.originalBitmap));
            bm = ImageViewActivity.originalBitmap;
            mCallback.communicate(ImageViewActivity.originalBitmap);
            ImageViewActivity.originalBitmap.recycle();
        });
    }

    //Vedere se tenere questo metodo/bottone
    private void clickZoom15() {
        //Richiesta immagine 1.5x
        fabZoom15.setOnClickListener(view -> {
            subsamplingScaleImageView.setMaxScale(1);
        });
    }
}