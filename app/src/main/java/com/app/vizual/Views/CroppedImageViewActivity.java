package com.app.vizual.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import com.app.vizual.APIResponse.ApiService;
import com.app.vizual.Interfaces.FragmentToActivity;
import com.app.vizual.Presenters.CroppedImagePresenter;
import com.app.vizual.Fragment.ZoomFragment;
import com.app.vizual.databinding.ActivityCroppedImageViewBinding;

import okhttp3.ResponseBody;
import retrofit2.Call;

//copiare image view ma con left top width ed height che ad ogni chiamta le viene sommata le vecchie coords
public class CroppedImageViewActivity extends AppCompatActivity implements FragmentToActivity {
    private ApiService apiService = new ApiService();
    private ActivityCroppedImageViewBinding binding;
    private int left, top, width, height;
    private String nameImage;
    public static Bitmap bmp;
    private boolean isFABOpen = false;
    public static Bitmap croppedGrayscaledImage;
    private CroppedImagePresenter croppedImagePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        croppedImagePresenter = new CroppedImagePresenter(this);

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

            new Handler(Looper.getMainLooper()).post(() -> {
                Call<ResponseBody> call = apiService.getObjRetrofit().getImageCropped(nameImage, left, top, width, height);
                apiService.callRetrofit(call, response -> {
                    if (response != null) {
                        bmp = BitmapFactory.decodeStream(response.byteStream());
                        croppedImagePresenter.replaceFragment(new ZoomFragment(bmp, nameImage, true), binding);
                        binding.progressBar.setVisibility(View.GONE);
                        binding.textView.setVisibility(View.GONE);
                        new Handler(Looper.getMainLooper()).post(() -> {
                            Call<ResponseBody> callGray = apiService.getObjRetrofit().getImageCroppedGrayscaled(nameImage, left, top, width, height);
                            apiService.callRetrofit(callGray, responseBody -> {
                                if (responseBody == null) {
                                    Log.d("DEBUG", "response null");
                                    return;
                                }
                                croppedGrayscaledImage = BitmapFactory.decodeStream(responseBody.byteStream());
                            });
                        });
                    }
                });
            });
        }

        //click to return to the home
        croppedImagePresenter.clickHomeButton(binding);
        //click to open the fragment and the visualization of the crop side
        croppedImagePresenter.clickCropButton(binding, nameImage);
        //click to open the floating button menu
        croppedImagePresenter.clickLogoButton(binding);
        //click to open the fragment and the visualization of the zoom side
        croppedImagePresenter.clickZoomButton(binding, nameImage);
    }

    public Activity getStartActivity(){
        return this;
    }

    @Override
    public void communicate(Bitmap data) {
        bmp = data;
    }
}