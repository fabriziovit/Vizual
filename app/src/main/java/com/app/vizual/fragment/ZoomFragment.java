package com.app.vizual.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.vizual.R;
import com.ortiz.touchview.TouchImageView;

public class ZoomFragment extends Fragment {
    View view;
    Bitmap bm;
    TouchImageView touchImageView;

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
        return view;
    }
}