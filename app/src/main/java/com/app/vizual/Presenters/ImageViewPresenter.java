package com.app.vizual.Presenters;

import static com.app.vizual.Views.ImageViewActivity.bm;

import android.content.Intent;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.app.vizual.Fragment.CropFragment;
import com.app.vizual.Fragment.ZoomFragment;
import com.app.vizual.Views.HomePageActivity;
import com.app.vizual.Views.ImageViewActivity;
import com.app.vizual.R;
import com.app.vizual.databinding.ActivityImageViewBinding;

public class ImageViewPresenter {
    private final ImageViewActivity imageViewActivity;
    private boolean isFABOpen = false;

    public ImageViewPresenter(ImageViewActivity imageViewActivity) {
        this.imageViewActivity = imageViewActivity;
    }

    public void clickLogoButton(ActivityImageViewBinding binding){
        binding.fabLogo.setOnClickListener(view -> {
            if (!isFABOpen) {
                showFABMenu(binding);
            } else {
                closeFABMenu(binding);
            }
        });
    }

    public void clickHomeButton(ActivityImageViewBinding binding){
        binding.fabHome.setOnClickListener(view -> {
            Intent intent = new Intent(imageViewActivity, HomePageActivity.class);
            imageViewActivity.getStartActivity().startActivity(intent);
            imageViewActivity.getStartActivity().finish();
        });
    }

    public void clickZoomButton(ActivityImageViewBinding binding, String currentSelection){
        binding.fabZoom.setOnClickListener(view -> {
            replaceFragment(new ZoomFragment(bm, currentSelection), binding);
        });
    }

    public void clickCropButton(ActivityImageViewBinding binding, String currentSelection){
        binding.fabCrop.setOnClickListener(view -> {
            replaceFragment(new CropFragment(bm, currentSelection), binding);
        });
    }

    private void showFABMenu(ActivityImageViewBinding binding) {
        isFABOpen = true;
        binding.fabZoom.setVisibility(View.VISIBLE);
        binding.fabCrop.setVisibility(View.VISIBLE);
        binding.fabHome.setVisibility(View.VISIBLE);
        binding.fabZoom.animate().translationY(-imageViewActivity.getResources().getDimension(R.dimen.standard_55));
        binding.fabCrop.animate().translationY(-imageViewActivity.getResources().getDimension(R.dimen.standard_100));
        binding.fabHome.animate().translationY(-imageViewActivity.getResources().getDimension(R.dimen.standard_145));
    }

    private void closeFABMenu(ActivityImageViewBinding binding) {
        isFABOpen = false;
        binding.fabZoom.animate().translationY(0);
        binding.fabCrop.animate().translationY(0);
        binding.fabHome.animate().translationY(0);
        binding.fabZoom.setVisibility(View.GONE);
        binding.fabCrop.setVisibility(View.GONE);
        binding.fabHome.setVisibility(View.GONE);
    }

    public void replaceFragment(Fragment fragment, ActivityImageViewBinding binding) {
        FragmentManager fragmentManager = imageViewActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(binding.frameContainer.getId(), fragment);
        fragmentTransaction.commit();
    }
}
