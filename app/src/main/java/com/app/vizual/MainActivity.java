package com.app.vizual;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.app.vizual.APIResponse.ApiService;
import com.app.vizual.Interfaces.ApiInterface;
import com.app.vizual.Models.ListImages;
import com.app.vizual.databinding.ActivityMainBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    public static ActivityMainBinding binding;
    private ArrayList<String> imagesName;
    ApiService apiService = new ApiService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Call<ListImages> call = apiService.getObjRetrofit().getImages();
        apiService.callRetrofit(call, response -> {
            if (response != null)
                imagesName = response.getData();
            else
                imagesName = new ArrayList<>(Collections.singletonList(getResources().getString(R.string.no_image_available)));
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_item, imagesName);
            binding.categorieAuto.setAdapter(arrayAdapter);
        });

        btnPressed(binding);
    }

    public void btnPressed(ActivityMainBinding binding) {
        binding.mainActivityButton.setOnClickListener(view -> {
            String currentSelection = binding.categorieAuto.getText().toString();
            if (!currentSelection.equals(getResources().getString(R.string.no_image_available)) &&
                    !currentSelection.equals(getResources().getString(R.string.choose_image))) {
                // Call API
                Call<ResponseBody> call = apiService.getObjRetrofit().getImage(currentSelection);
                apiService.callRetrofit(call, response -> {
                    if (response == null) {
                        Log.d("DEBUG", "response null");
                        return;
                    }
                    Bitmap bmp = BitmapFactory.decodeStream(response.byteStream());
                    binding.imageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, 256, 256, false));
                });
            }
        });
    }
}