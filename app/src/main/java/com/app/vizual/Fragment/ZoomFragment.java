package com.app.vizual.Fragment;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.app.vizual.Presenters.ZoomPresenter;
import com.app.vizual.Util.CustomGlideApp;
import com.app.vizual.Interfaces.FragmentToActivity;
import com.app.vizual.R;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class ZoomFragment extends Fragment{
    public static Bitmap bm;
    private String nameImage;
    private SubsamplingScaleImageView subsamplingScaleImageView;
    private FloatingActionButton fabMenuZoom;
    private FragmentToActivity mCallback;
    private NavigationView navMenu;
    private Switch drawerSwitch;
    public static int left = 0, top = 0;
    private ZoomPresenter zoomPresenter;

    public ZoomFragment() {
        // Required empty public constructor
    }

    public ZoomFragment(Bitmap bitmap, String nameImage) {
        bm = bitmap;
        this.nameImage = nameImage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zoom, container, false);
        mCallback = (FragmentToActivity) getContext();
        zoomPresenter = new ZoomPresenter(this);

        subsamplingScaleImageView = view.findViewById(R.id.zoomImageView);
        subsamplingScaleImageView.setMaxScale(10.0f);
        CustomGlideApp glideApp = new CustomGlideApp();
        glideApp.init(getContext(), bm, subsamplingScaleImageView);
        fabMenuZoom = view.findViewById(R.id.fabPassImageZoomed);
        navMenu = view.findViewById(R.id.mDrawerLayout);
        navMenu.setItemIconTintList(null);
        MenuItem menuItem = navMenu.getMenu().findItem(R.id.grayscale); // This is the menu item that contains your switch
        drawerSwitch = (Switch) menuItem.getActionView();
        drawerSwitch.setClickable(false);

        zoomPresenter.itemSelected(navMenu, drawerSwitch, subsamplingScaleImageView, mCallback);

        zoomPresenter.clickOpenMenu(navMenu, fabMenuZoom, drawerSwitch, subsamplingScaleImageView, mCallback);
        zoomPresenter.setProgressDialog();
        zoomPresenter.switchClicked(drawerSwitch);

        return view;
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
}