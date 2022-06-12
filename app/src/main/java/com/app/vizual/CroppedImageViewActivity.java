package com.app.vizual;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import com.app.vizual.APIResponse.ApiService;
import com.app.vizual.databinding.ActivityCroppedImageViewBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;

import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class CroppedImageViewActivity extends AppCompatActivity{
    private ApiService apiService = new ApiService();
    private ActivityCroppedImageViewBinding binding;
    private int left, top, width, height;
    private String nameImage;
    private Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCroppedImageViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //GetExtra per prendere i dati per eseguire la chimata API
        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("nameImage") != null) {
            nameImage = bundle.getString("nameImage");
            left = bundle.getInt("left");
            top = bundle.getInt("top");
            width = bundle.getInt("width");
            height = bundle.getInt("height");
        }

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Call<ResponseBody> call = apiService.getObjRetrofit().getImageCropped(nameImage, left, top, width, height);
            apiService.callRetrofit(call, response -> {
                if (response == null) {
                    Log.d("DEBUG", "response null");
                    return;
                }
                binding.progressBar.setVisibility(View.VISIBLE);
                bmp = BitmapFactory.decodeStream(response.byteStream());

                //errore
                CustomGlideApp glideApp = new CustomGlideApp();
                glideApp.init(getApplicationContext(), bmp, binding.zoomImageView);

                binding.progressBar.setVisibility(View.GONE);
                binding.textView.setVisibility(View.GONE);
            });
        }

        binding.fabHome.setOnClickListener(view -> {
            Intent intent = new Intent(CroppedImageViewActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}