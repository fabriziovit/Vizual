package com.app.vizual.Views;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.app.vizual.APIResponse.ApiService;

import androidx.appcompat.app.AppCompatActivity;

import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import com.app.vizual.Interfaces.FragmentToActivity;
import com.app.vizual.Models.IntegerModel;
import com.app.vizual.Presenters.ImageViewPresenter;
import com.app.vizual.databinding.ActivityImageViewBinding;
import com.app.vizual.Fragment.ZoomFragment;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class ImageViewActivity extends AppCompatActivity implements FragmentToActivity {
    private ActivityImageViewBinding binding;
    private ApiService apiService = new ApiService();
    private String currentSelection;
    private boolean isFABOpen = false;
    public static Bitmap originalBitmap;
    public static Bitmap bm;
    public static int widthOriginal, heightOriginal;
    public final static int maxHeight = 3007, maxWidth = 5120;
    public static double ratio;
    public static double originalRatio;
    private ImageViewPresenter imageViewPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageViewPresenter = new ImageViewPresenter(this);
        binding = ActivityImageViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        System.out.println(displayMetrics.heightPixels+"   "+
        displayMetrics.widthPixels);
        //1504 2560
         */

        //GetExtra per prendere il nome dell'immagine ed eseguire le API
        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("currentSelection") != null) {
            currentSelection = bundle.getString("currentSelection");
        }

        // Call API
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Call<ResponseBody> call = apiService.getObjRetrofit().getImage(currentSelection);
            apiService.callRetrofit(call, response -> {
                if (response != null) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    bm = BitmapFactory.decodeStream(response.byteStream());
                    originalBitmap = bm;
                }
                Call<IntegerModel> callWidth = apiService.getObjRetrofit().getWidth(currentSelection);
                apiService.callRetrofit(callWidth, responseWidth ->{
                    if (responseWidth != null) {
                        widthOriginal = responseWidth.getData().get(0);
                        Call<IntegerModel> callHeight = apiService.getObjRetrofit().getHeight(currentSelection);
                        apiService.callRetrofit(callHeight, responseHeight -> {
                            if (responseHeight != null) {
                                heightOriginal = responseHeight.getData().get(0);
                                if(heightOriginal != 0 && widthOriginal != 0) {
                                    ratio = (double)Math.max((double)heightOriginal / maxHeight, (double)widthOriginal / maxWidth);
                                    originalRatio = (double)Math.max((double)heightOriginal / maxHeight, (double)widthOriginal / maxWidth);
                                }
                            }
                        });
                    }
                });

                imageViewPresenter.replaceFragment(new ZoomFragment(bm, currentSelection), binding);
                binding.progressBar.setVisibility(View.GONE);
                binding.textView.setVisibility(View.GONE);
            });
        }

        imageViewPresenter.clickLogoButton(binding);
        imageViewPresenter.clickCropButton(binding, currentSelection);
        imageViewPresenter.clickZoomButton(binding, currentSelection);
        imageViewPresenter.clickHomeButton(binding);
    }

    public Activity getStartActivity(){
        return this;
    }

    @Override
    public void communicate(Bitmap data) {
        bm = data;
    }
}