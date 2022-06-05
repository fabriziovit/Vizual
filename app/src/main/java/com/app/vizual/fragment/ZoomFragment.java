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
import com.app.vizual.Interfaces.FragmentToActivity;
import com.app.vizual.R;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ZoomFragment extends Fragment{
    View view;
    Bitmap bm;
    SubsamplingScaleImageView subsamplingScaleImageView;
    FloatingActionButton fabZoom1, fabZoom15, fabZoom2;
    private FragmentToActivity mCallback;

    public ZoomFragment() {
        // Required empty public constructor
    }

    public ZoomFragment(Bitmap bitmap) {
        bm = bitmap;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_zoom, container, false);
        mCallback = (FragmentToActivity) getContext();

        subsamplingScaleImageView = view.findViewById(R.id.zoomImageView);
        CustomGlideApp glideApp = new CustomGlideApp();
        glideApp.init(this, bm, subsamplingScaleImageView);
        fabZoom1 = view.findViewById(R.id.fabZoom1);
        fabZoom15 = view.findViewById(R.id.fabZoom15);
        fabZoom2 = view.findViewById(R.id.fabZoom25);

        clickZoom25();
        /*
        clickZoom1();
        clickZoom15();

         */

        return view;
    }

    private void clickZoom1() {
        //Richiesta immagine 1x
        fabZoom1.setOnClickListener(view -> {
            subsamplingScaleImageView.setMaxScale(0.5f);
            /*touchImageView.setMaxZoom(1);
            touchImageView.setMinZoom(1);
            touchImageView.setZoom(1);

             */
        });
    }

    private void clickZoom15() {
        //Richiesta immagine 1.5x
        fabZoom15.setOnClickListener(view -> {
            subsamplingScaleImageView.setMaxScale(1);
            /*touchImageView.setMaxZoom(1.5f);
            touchImageView.setMinZoom(1.5f);
            touchImageView.setZoom(1.5f);
             */
        });
    }

    private void clickZoom25() {
        //Richiesta immagine 2.5x
        fabZoom2.setOnClickListener(view -> {
            PointF leftTopCoord = subsamplingScaleImageView.viewToSourceCoord(new PointF(0, 0));
            PointF rightBottomCoord = subsamplingScaleImageView.viewToSourceCoord(new PointF(subsamplingScaleImageView.getWidth(), subsamplingScaleImageView.getHeight()));
            RectF visibleRectF = new RectF(leftTopCoord.x, leftTopCoord.y, rightBottomCoord.x, rightBottomCoord.y);
            System.out.println(visibleRectF);
            final Rect visibleRect = new Rect();
            visibleRectF.round(visibleRect);
            System.out.println("Zoom: "+visibleRect.left+" "+visibleRect.top+" "+visibleRect.width()+"  "+visibleRect.height());
            System.out.println(visibleRect.right);
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
}