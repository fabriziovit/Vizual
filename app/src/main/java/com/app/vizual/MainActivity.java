package com.app.vizual;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.app.vizual.APIResponse.ApiService;
import com.app.vizual.Models.ListImages;
import com.app.vizual.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Collections;

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

                Intent intent = new Intent(MainActivity.this, ImageViewActivity.class);
                intent.putExtra("currentSelection", currentSelection);
                startActivity(intent);
            }else
                Toast.makeText(getApplicationContext(), "Scegli un immagine!", Toast.LENGTH_SHORT).show();
        });
    }
}