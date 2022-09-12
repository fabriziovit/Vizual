package com.app.vizual.Presenters;

import static com.app.vizual.Views.CroppedImageViewActivity.bmp;

import android.content.Intent;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.app.vizual.Views.CroppedImageViewActivity;
import com.app.vizual.Fragment.CropFragment;
import com.app.vizual.Fragment.ZoomFragment;
import com.app.vizual.HomePageActivity;
import com.app.vizual.R;
import com.app.vizual.databinding.ActivityCroppedImageViewBinding;

public class CroppedImagePresenter {
    private final CroppedImageViewActivity croppedImageViewActivity;
    private boolean isFABOpen = false;

    public CroppedImagePresenter(CroppedImageViewActivity croppedImageViewActivity) {
        this.croppedImageViewActivity = croppedImageViewActivity;
    }

    public void clickLogoButton(ActivityCroppedImageViewBinding binding) {
        binding.fabLogo.setOnClickListener(view ->
        {
            if (!isFABOpen) {
                showFABMenu(binding);
            } else {
                closeFABMenu(binding);
            }
        });
    }

    public void clickHomeButton(ActivityCroppedImageViewBinding binding){
        binding.fabHome.setOnClickListener(view -> {
            Intent intent = new Intent(croppedImageViewActivity, HomePageActivity.class);
            croppedImageViewActivity.getStartActivity().startActivity(intent);
            croppedImageViewActivity.getStartActivity().finish();
        });
    }

    public void clickCropButton(ActivityCroppedImageViewBinding binding, String nameImage){
        binding.fabCrop.setOnClickListener(view -> {
            replaceFragment(new CropFragment(bmp, nameImage), binding);
        });
    }

    public void clickZoomButton(ActivityCroppedImageViewBinding binding, String nameImage){
        binding.fabZoom.setOnClickListener(view -> {
            replaceFragment(new ZoomFragment(bmp, nameImage), binding);
        });
    }

    private void showFABMenu(ActivityCroppedImageViewBinding binding) {
        isFABOpen = true;
        binding.fabZoom.setVisibility(View.VISIBLE);
        binding.fabCrop.setVisibility(View.VISIBLE);
        binding.fabHome.setVisibility(View.VISIBLE);
        binding.fabZoom.animate().translationY(-croppedImageViewActivity.getResources().getDimension(R.dimen.standard_55));
        binding.fabCrop.animate().translationY(-croppedImageViewActivity.getResources().getDimension(R.dimen.standard_100));
        binding.fabHome.animate().translationY(-croppedImageViewActivity.getResources().getDimension(R.dimen.standard_145));
    }

    private void closeFABMenu(ActivityCroppedImageViewBinding binding) {
        isFABOpen = false;
        binding.fabZoom.animate().translationY(0);
        binding.fabCrop.animate().translationY(0);
        binding.fabHome.animate().translationY(0);
        binding.fabZoom.setVisibility(View.GONE);
        binding.fabCrop.setVisibility(View.GONE);
        binding.fabHome.setVisibility(View.GONE);
    }

    public void replaceFragment(Fragment fragment, ActivityCroppedImageViewBinding binding) {
        FragmentManager fragmentManager = croppedImageViewActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(binding.frameContainer.getId(), fragment);
        fragmentTransaction.commit();
    }
}
