package com.app.vizual.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.app.vizual.CroppedImageViewActivity;
import com.app.vizual.CustomGlideApp;
import com.app.vizual.ImageViewActivity;
import com.app.vizual.Interfaces.FragmentToActivity;
import com.app.vizual.R;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ZoomFragment extends Fragment{
    private Bitmap bm;
    private String nameImage;
    private SubsamplingScaleImageView subsamplingScaleImageView;
    private FloatingActionButton fabMenu;
    private FragmentToActivity mCallback;
    private AlertDialog dialog;
    private NavigationView navMenu;
    private boolean isMenuOpen = false;
    public static int left = 0, top = 0;
    private boolean flag = false;
    private Switch drawerSwitch;

    public ZoomFragment() {
        // Required empty public constructor
    }

    public ZoomFragment(Bitmap bitmap, String nameImage) {
        bm = bitmap;
        this.nameImage = nameImage;
    }

    public ZoomFragment(Bitmap bitmap, String nameImage, boolean bool) {
        bm = bitmap;
        this.nameImage = nameImage;
        flag = bool;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zoom, container, false);
        mCallback = (FragmentToActivity) getContext();

        subsamplingScaleImageView = view.findViewById(R.id.zoomImageView);
        subsamplingScaleImageView.setMaxScale(10.0f);
        CustomGlideApp glideApp = new CustomGlideApp();
        glideApp.init(getContext(), bm, subsamplingScaleImageView);
        fabMenu = view.findViewById(R.id.fabMenu);
        navMenu = view.findViewById(R.id.mDrawerLayout);
        navMenu.setItemIconTintList(null);
        MenuItem menuItem = navMenu.getMenu().findItem(R.id.grayscale); // This is the menu item that contains the switch
        drawerSwitch = (Switch) menuItem.getActionView();

        //if the fragment is in the crop activity pass zoomed image can't be clicked
        if(flag)
            navMenu.getMenu().findItem(R.id.passImageZoomed).setVisible(false);

        itemSelected();
        clickOpenMenu();
        setProgressDialog();
        switchClicked();

        return view;
    }

    //get Image zoomed with screen size
    private void clickGetImageZoomed() {
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
    }

    //Reset zoom and position in the image
    private void clickResetImage() {
        subsamplingScaleImageView.setImage(ImageSource.cachedBitmap(ImageViewActivity.originalBitmap));
        bm = ImageViewActivity.originalBitmap;
        mCallback.communicate(ImageViewActivity.originalBitmap);
    }

    private void itemSelected(){
        navMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                itemClickedMenu(item.getItemId());
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        navMenu.setCheckedItem(R.id.grayscale);
        navMenu.getMenu().performIdentifierAction(R.id.grayscale, 0);
        if (onOptionsItemSelected(item)) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void switchClicked(){
        drawerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if(flag){
                        //passing the cropped grayscaled image
                        CustomGlideApp glideApp = new CustomGlideApp();
                        glideApp.init(getContext(), CroppedImageViewActivity.croppedGrayscaledImage, subsamplingScaleImageView);
                    }else{
                        //passing the grayscaled image
                        CustomGlideApp glideApp = new CustomGlideApp();
                        glideApp.init(getContext(), ImageViewActivity.grayscaledImage, subsamplingScaleImageView);
                    }
                } else {
                    //passing the image with original colors
                    CustomGlideApp glideApp = new CustomGlideApp();
                    glideApp.init(getContext(), bm, subsamplingScaleImageView);
                }
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    private void itemClickedMenu(int id){
        switch (id){
            case R.id.originalButton:
                clickResetImage();
                break;
            case R.id.passImageZoomed:
                clickGetImageZoomed();
                navMenu.getMenu().findItem(R.id.grayscale).setEnabled(false);
                drawerSwitch.setEnabled(false);
                break;
            case R.id.grayscale:
                if(drawerSwitch.isChecked()){
                    //passing the image with originals colors
                    drawerSwitch.setChecked(false);
                    CustomGlideApp glideApp = new CustomGlideApp();
                    glideApp.init(getContext(), bm, subsamplingScaleImageView);
                }else{
                    drawerSwitch.setChecked(true);
                    if(flag){
                        //passing the cropped grayscale image
                        CustomGlideApp glideApp = new CustomGlideApp();
                        glideApp.init(getContext(), CroppedImageViewActivity.croppedGrayscaledImage, subsamplingScaleImageView);
                    }else{
                        //passing the grayscale image
                        CustomGlideApp glideApp = new CustomGlideApp();
                        glideApp.init(getContext(), ImageViewActivity.grayscaledImage, subsamplingScaleImageView);
                    }
                }
                break;
        }
    }

    private void clickOpenMenu(){
        fabMenu.setOnClickListener(view -> {
            if (!isMenuOpen) {
                showMenu();
                fabMenu.setImageResource(R.drawable.ic_close_menu_40dp);
            } else {
                closeMenu();
                fabMenu.setImageResource(R.drawable.ic_menu_40dp);
            }
        });
    }

    private void showMenu(){
        isMenuOpen = true;
        navMenu.setVisibility(View.VISIBLE);

    }

    private void closeMenu(){
        isMenuOpen = false;
        navMenu.setVisibility(View.GONE);
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