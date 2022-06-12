package com.app.vizual.fragment;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    private AlertDialog dialog;
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
        glideApp.init(getContext(), bm, subsamplingScaleImageView);
        fabPassImage = view.findViewById(R.id.fabPassImageZoomed);
        fabZoom15 = view.findViewById(R.id.fabZoom15);
        fabOriginalImage = view.findViewById(R.id.fabOriginalImage);

        clickReset();
        clickGetImageResized();
        setProgressDialog();
        //clickZoom15();

        return view;
    }

    //get Image zoomed with screen size
    private void clickGetImageResized() {
        fabPassImage.setOnClickListener(view -> {
            dialog.show();
            new Handler(Looper.getMainLooper()).post(() -> {
                PointF leftTopCoord = subsamplingScaleImageView.viewToSourceCoord(new PointF(0, 0));
                PointF rightBottomCoord = subsamplingScaleImageView.viewToSourceCoord(new PointF(subsamplingScaleImageView.getWidth(), subsamplingScaleImageView.getHeight()));
                RectF visibleRectF = new RectF(leftTopCoord.x, leftTopCoord.y, rightBottomCoord.x, rightBottomCoord.y);
                final Rect visibleRect = new Rect();
                visibleRectF.round(visibleRect);
                left = visibleRect.left;
                top = visibleRect.top;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte[] bitmapdata = bos.toByteArray();
                ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
                BitmapRegionDecoder decoder = null;
                try {
                    decoder = BitmapRegionDecoder.newInstance(bs, false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Bitmap visibleWallBitmap = decoder.decodeRegion(visibleRect, null);
                bm = visibleWallBitmap;
                subsamplingScaleImageView.setImage(ImageSource.bitmap(visibleWallBitmap));
                dialog.cancel();
                mCallback.communicate(visibleWallBitmap);
            });
        });
    }

    //Da Fixare
    //Reset zoom and position in the image
    private void clickReset() {
        //Richiesta immagine 2.5x
        fabOriginalImage.setOnClickListener(view -> {
            subsamplingScaleImageView.setImage(ImageSource.cachedBitmap(ImageViewActivity.originalBitmap));
            bm = ImageViewActivity.originalBitmap;
            mCallback.communicate(ImageViewActivity.originalBitmap);
        });
    }

    //Vedere se tenere questo metodo/bottone
    private void clickZoom15() {
        //Richiesta immagine 1.5x
        fabZoom15.setOnClickListener(view -> {
            subsamplingScaleImageView.setMaxScale(1);
        });
    }

    public void setProgressDialog() {
        int llPadding = 30;
        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setPadding(llPadding, llPadding, llPadding, llPadding);
        ll.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams llParam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        ll.setLayoutParams(llParam);

        ProgressBar progressBar = new ProgressBar(getContext());
        progressBar.setIndeterminate(true);
        progressBar.setPadding(0, 0, llPadding, 0);
        progressBar.setLayoutParams(llParam);

        llParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        TextView tvText = new TextView(getContext());
        tvText.setText("Caricamento ...");
        tvText.setTextColor(Color.parseColor("#000000"));
        tvText.setTextSize(20);
        tvText.setLayoutParams(llParam);

        ll.addView(progressBar);
        ll.addView(tvText);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setView(ll);

        dialog = builder.create();
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(layoutParams);
        }
    }
}