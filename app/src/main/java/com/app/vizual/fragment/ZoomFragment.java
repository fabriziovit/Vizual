package com.app.vizual.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.vizual.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ortiz.touchview.TouchImageView;

public class ZoomFragment extends Fragment {
    View view;
    Bitmap bm;
    TouchImageView touchImageView;
    FloatingActionButton fabZoom1,fabZoom15, fabZoom2;


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
        touchImageView = view.findViewById(R.id.zoomImageView);
        touchImageView.setImageBitmap(bm);
        touchImageView.setMaxZoom(1);
        fabZoom1 = view.findViewById(R.id.fabZoom1);
        fabZoom15 = view.findViewById(R.id.fabZoom15);
        fabZoom2 = view.findViewById(R.id.fabZoom25);

        clickZoom25();
        clickZoom1();
        clickZoom15();

        return view;
    }

    private void clickZoom1(){
        //Richiesta immagine 1x
        fabZoom1.setOnClickListener(view -> {
            touchImageView.setMaxZoom(1);
            touchImageView.setMinZoom(1);
            touchImageView.setZoom(1);
        });
    }

    private void clickZoom15(){
        //Richiesta immagine 1.5x
        fabZoom15.setOnClickListener(view -> {
            touchImageView.setMaxZoom(1.5f);
            touchImageView.setMinZoom(1.5f);
            touchImageView.setZoom(1.5f);
        });
    }

    private void clickZoom25(){
        //Richiesta immagine 2.5x
        fabZoom2.setOnClickListener(view -> {
            touchImageView.setMaxZoom(2.5f);
            touchImageView.setMinZoom(2.5f);
            touchImageView.setZoom(2.5f);
        });
    }
}