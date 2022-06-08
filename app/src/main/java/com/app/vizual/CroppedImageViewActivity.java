package com.app.vizual;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import com.app.vizual.APIResponse.ApiService;
import com.app.vizual.Interfaces.FragmentToActivity;
import com.app.vizual.databinding.ActivityCroppedImageViewBinding;
import com.app.vizual.databinding.ActivityImageViewBinding;
import com.app.vizual.fragment.ZoomFragment;
import com.davemorrissey.labs.subscaleview.ImageSource;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class CroppedImageViewActivity extends AppCompatActivity {
    private ApiService apiService = new ApiService();
    private ActivityCroppedImageViewBinding binding;
    private int left, top, width, height;
    String nameImage;
    Bitmap bmp;

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
                binding.zoomImageView.setImage(ImageSource.bitmap(bmp));
                binding.progressBar.setVisibility(View.GONE);
                binding.textView.setVisibility(View.GONE);
                //immagine ridimensiionata: dim immagine originale width: 32001 height: 38474 ratio 0.03909133440765192
            });
        }

        binding.fabHome.setOnClickListener(view -> {
            Intent intent = new Intent(CroppedImageViewActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}