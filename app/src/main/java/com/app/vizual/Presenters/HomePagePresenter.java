package com.app.vizual.Presenters;

import android.content.Intent;
import android.widget.Toast;

import com.app.vizual.R;
import com.app.vizual.Views.HomePageActivity;
import com.app.vizual.Views.ImageViewActivity;
import com.app.vizual.databinding.ActivityMainBinding;

public class HomePagePresenter {
    private final HomePageActivity homePageActivity;

    public HomePagePresenter(HomePageActivity homePageActivity) {
        this.homePageActivity = homePageActivity;
    }

    public void buttonPressed(ActivityMainBinding binding) {
        binding.mainActivityButton.setOnClickListener(view -> {
            String currentSelection = binding.categorieAuto.getText().toString();
            if (!currentSelection.equals(homePageActivity.getResources().getString(R.string.no_image_available)) &&
                    !currentSelection.equals(homePageActivity.getResources().getString(R.string.choose_image))) {

                Intent intent = new Intent(homePageActivity, ImageViewActivity.class);
                intent.putExtra("currentSelection", currentSelection);
                homePageActivity.getStartActivity().startActivity(intent);
            }else
                Toast.makeText(homePageActivity.getApplicationContext(), "Scegli un immagine!", Toast.LENGTH_SHORT).show();
        });
    }
}
