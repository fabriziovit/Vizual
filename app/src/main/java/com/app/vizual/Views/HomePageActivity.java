package com.app.vizual.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.app.vizual.APIResponse.ApiService;
import com.app.vizual.Models.ListImages;
import com.app.vizual.Presenters.HomePagePresenter;
import com.app.vizual.R;
import com.app.vizual.databinding.ActivityMainBinding;
import com.app.vizual.Fragment.CropFragment;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;

public class HomePageActivity extends AppCompatActivity {
    public static ActivityMainBinding binding;
    private ArrayList<String> imagesName;
    private ApiService apiService = new ApiService();
    private HomePagePresenter homePagePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homePagePresenter = new HomePagePresenter(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        CropFragment.left = 0;
        CropFragment.top = 0;

        //api call to load the list of the image that are saved on the server
        Call<ListImages> call = apiService.getObjRetrofit().getImages();
        apiService.callRetrofit(call, response -> {
            if (response != null)
                imagesName = response.getData();
            else
                imagesName = new ArrayList<>(Collections.singletonList(getResources().getString(R.string.no_image_available)));
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_item, imagesName);
            binding.categorieAuto.setAdapter(arrayAdapter);
        });

        //when selected an image open the activity to display the image choosen
        homePagePresenter.buttonPressed(binding);
    }

    public Activity getStartActivity(){
        return this;
    }
}