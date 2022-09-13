package com.app.vizual.Presenters;

import static com.app.vizual.Fragment.ZoomFragment.flag;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.app.vizual.Fragment.CropFragment;
import com.app.vizual.Fragment.ZoomFragment;
import com.app.vizual.R;
import com.app.vizual.Util.CustomGlideApp;
import com.app.vizual.Views.CroppedImageViewActivity;
import com.app.vizual.Views.ImageViewActivity;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ZoomPresenter {
    private final ZoomFragment zoomFragment;
    private boolean flagSwitch = false;
    private boolean isMenuOpen = false;
    private AlertDialog dialog;

    public ZoomPresenter(ZoomFragment zoomFragment) {
        this.zoomFragment = zoomFragment;
    }

    //get Image zoomed with screen size
    private void clickGetImageZoomed(SubsamplingScaleImageView subsamplingScaleImageView) {
        dialog.show();
        new Handler(Looper.getMainLooper()).post(() -> {
            PointF leftTopCoord = subsamplingScaleImageView.viewToSourceCoord(new PointF(0, 0));
            PointF rightBottomCoord = subsamplingScaleImageView.viewToSourceCoord(new PointF(subsamplingScaleImageView.getWidth(), subsamplingScaleImageView.getHeight()));
            RectF visibleRectF = new RectF(leftTopCoord.x, leftTopCoord.y, rightBottomCoord.x, rightBottomCoord.y);
            final Rect visibleRect = new Rect();
            visibleRectF.round(visibleRect);
            ZoomFragment.left = visibleRect.left;
            ZoomFragment.top = visibleRect.top;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ZoomFragment.bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] bitmapdata = bos.toByteArray();
            ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
            BitmapRegionDecoder decoder = null;
            try {
                decoder = BitmapRegionDecoder.newInstance(bs, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap visibleWallBitmap = decoder.decodeRegion(visibleRect, null);
            ZoomFragment.bm = visibleWallBitmap;
            subsamplingScaleImageView.setImage(ImageSource.bitmap(visibleWallBitmap));
            dialog.cancel();
            ImageViewActivity.bm = visibleWallBitmap;;
        });
    }

    //Reset zoom and position in the image
    private void clickResetImage(SubsamplingScaleImageView subsamplingScaleImageView) {
        subsamplingScaleImageView.setImage(ImageSource.cachedBitmap(ImageViewActivity.originalBitmap));
        ZoomFragment.bm = ImageViewActivity.originalBitmap;
        ImageViewActivity.bm = ImageViewActivity.originalBitmap;
        CropFragment.bm = ImageViewActivity.originalBitmap;
        CroppedImageViewActivity.bmp = ImageViewActivity.originalBitmap;
    }

    public void itemSelected(NavigationView navMenu, Switch drawerSwitch, SubsamplingScaleImageView subsamplingScaleImageView){
        navMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                itemClickedMenu(item.getItemId(), navMenu, drawerSwitch,  subsamplingScaleImageView);
                return true;
            }
        });
    }

    public void switchClicked(Switch drawerSwitch, SubsamplingScaleImageView subsamplingScaleImageView){
        drawerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if(flag){
                        //passing the cropped grayscaled image
                        CustomGlideApp glideApp = new CustomGlideApp();
                        glideApp.init(zoomFragment.getContext(), CroppedImageViewActivity.croppedGrayscaledImage, subsamplingScaleImageView);
                    }else{
                        //passing the grayscaled image
                        CustomGlideApp glideApp = new CustomGlideApp();
                        glideApp.init(zoomFragment.getContext(), ImageViewActivity.grayscaledImage, subsamplingScaleImageView);
                    }
                } else {
                    //passing the image with original colors
                    CustomGlideApp glideApp = new CustomGlideApp();
                    glideApp.init(zoomFragment.getContext(), ZoomFragment.bm, subsamplingScaleImageView);
                }
            }
        });
    }

    private void itemClickedMenu(int id, NavigationView navMenu, Switch drawerSwitch, SubsamplingScaleImageView subsamplingScaleImageView){
        switch (id){
            case R.id.originalButton:
                clickResetImage(subsamplingScaleImageView);
                break;
            case R.id.passImageZoomed:
                clickGetImageZoomed(subsamplingScaleImageView);
                navMenu.getMenu().findItem(R.id.grayscale).setEnabled(false);
                drawerSwitch.setEnabled(false);
                break;
            case R.id.grayscale:
                if(drawerSwitch.isChecked()){
                    //passing the image with originals colors
                    drawerSwitch.setChecked(false);
                    CustomGlideApp glideApp = new CustomGlideApp();
                    glideApp.init(zoomFragment.getContext(), ZoomFragment.bm, subsamplingScaleImageView);
                }else{
                    drawerSwitch.setChecked(true);
                    if(flag){
                        //passing the cropped grayscale image
                        CustomGlideApp glideApp = new CustomGlideApp();
                        glideApp.init(zoomFragment.getContext(), CroppedImageViewActivity.croppedGrayscaledImage, subsamplingScaleImageView);
                    }else{
                        //passing the grayscale image
                        CustomGlideApp glideApp = new CustomGlideApp();
                        glideApp.init(zoomFragment.getContext(), ImageViewActivity.grayscaledImage, subsamplingScaleImageView);
                    }
                }
                break;
        }
    }

    public void clickOpenMenu(FloatingActionButton fabMenu, NavigationView navMenu){
        fabMenu.setOnClickListener(view -> {
            if (!isMenuOpen) {
                showMenu(navMenu);
                fabMenu.setImageResource(R.drawable.ic_close_menu_40dp);
            } else {
                closeMenu(navMenu);
                fabMenu.setImageResource(R.drawable.ic_menu_40dp);
            }
        });
    }

    private void showMenu(NavigationView navMenu){
        isMenuOpen = true;
        navMenu.setVisibility(View.VISIBLE);

    }

    private void closeMenu(NavigationView navMenu){
        isMenuOpen = false;
        navMenu.setVisibility(View.GONE);
    }

    public void setProgressDialog() {
        int llPadding = 30;
        LinearLayout ll = new LinearLayout(zoomFragment.getContext());
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setPadding(llPadding, llPadding, llPadding, llPadding);
        ll.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams llParam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        ll.setLayoutParams(llParam);

        ProgressBar progressBar = new ProgressBar(zoomFragment.getContext());
        progressBar.setIndeterminate(true);
        progressBar.setPadding(0, 0, llPadding, 0);
        progressBar.setLayoutParams(llParam);

        llParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        TextView tvText = new TextView(zoomFragment.getContext());
        tvText.setText("Caricamento ...");
        tvText.setTextColor(Color.parseColor("#000000"));
        tvText.setTextSize(20);
        tvText.setLayoutParams(llParam);

        ll.addView(progressBar);
        ll.addView(tvText);

        AlertDialog.Builder builder = new AlertDialog.Builder(zoomFragment.getContext());
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
