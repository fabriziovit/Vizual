package com.app.vizual.Fragment;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

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
    private FloatingActionButton fabMenu;
    private FragmentToActivity mCallback;
    private NavigationView navMenu;
    private boolean isMenuOpen = false;
    public static int left = 0, top = 0;
    public static boolean flag = false;
    private Switch drawerSwitch;
    private ZoomPresenter zoomPresenter;

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
        zoomPresenter = new ZoomPresenter(this);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

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
            if (flag)
                navMenu.getMenu().findItem(R.id.passImageZoomed).setVisible(false);

            //get the id to perform the selected action
            zoomPresenter.itemSelected(navMenu, drawerSwitch, subsamplingScaleImageView);
            //click to open menu
            zoomPresenter.clickOpenMenu(fabMenu, navMenu);
            //set the dialog window for the loading of the zoomed area
            zoomPresenter.setProgressDialog();
            //switch to select image with original color or grayscaled
            zoomPresenter.switchClicked(drawerSwitch, subsamplingScaleImageView);
        }

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