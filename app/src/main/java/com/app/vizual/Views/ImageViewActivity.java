package com.app.vizual.Views;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.app.vizual.APIResponse.ApiService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.View;

import com.app.vizual.Interfaces.FragmentToActivity;
import com.app.vizual.Models.Image;
import com.app.vizual.Models.IntegerModel;
import com.app.vizual.Presenters.ImageViewPresenter;
import com.app.vizual.Fragment.ZoomFragment;
import com.app.vizual.databinding.ActivityImageViewBinding;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class ImageViewActivity extends AppCompatActivity implements FragmentToActivity {
    private ActivityImageViewBinding binding;
    private ApiService apiService = new ApiService();
    private Image image;
    private FragmentManager fragmentManager;
    public static Bitmap originalBitmap;
    public static Bitmap bm;
    public static Bitmap grayscaledImage;
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

        //GetExtra per prendere il nome dell'immagine ed eseguire le API
        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("currentSelection") != null) {
            image = new Image(bundle.getString("currentSelection"));
        }

        // Call API
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Call<ResponseBody> call = apiService.getObjRetrofit().getImage(image.getName());
            apiService.callRetrofit(call, response -> {
                if (response != null) {
                    bm = BitmapFactory.decodeStream(response.byteStream());
                    originalBitmap = bm;

                    Call<IntegerModel> callWidth = apiService.getObjRetrofit().getWidth(image.getName());
                    apiService.callRetrofit(callWidth, responseWidth -> {
                        if (responseWidth != null) {
                            widthOriginal = responseWidth.getData().get(0);
                            Call<IntegerModel> callHeight = apiService.getObjRetrofit().getHeight(image.getName());
                            apiService.callRetrofit(callHeight, responseHeight -> {
                                if (responseHeight != null) {
                                    heightOriginal = responseHeight.getData().get(0);
                                    System.out.println(heightOriginal + "   " + widthOriginal);
                                    if (heightOriginal != 0 && widthOriginal != 0) {
                                        ratio = (double) Math.max((double) heightOriginal / maxHeight, (double) widthOriginal / maxWidth);
                                        originalRatio = ratio;
                                    }
                                }
                            });
                        }
                    });
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Call<ResponseBody> callGray = apiService.getObjRetrofit().getImageGrayscaled(image.getName());
                        apiService.callRetrofit(callGray, responseBody -> {
                            if (responseBody != null) {
                                grayscaledImage = BitmapFactory.decodeStream(responseBody.byteStream());
                            }
                        });
                    });
                    imageViewPresenter.replaceFragment(new ZoomFragment(bm, image.getName()), binding);
                    binding.progressBar.setVisibility(View.GONE);
                    binding.textView.setVisibility(View.GONE);
                }
            });
        }

        //click to open the fragment and the visualization of the crop side
        imageViewPresenter.clickCropButton(binding, image.getName());
        //click to return to the home
        imageViewPresenter.clickHomeButton(binding);
        //click to open the floating button menu
        imageViewPresenter.clickLogoButton(binding);
        //click to open the fragment and the visualization of the zoom side
        imageViewPresenter.clickZoomButton(binding, image.getName());
    }

    public Activity getStartActivity(){
        return this;
    }

    @Override
    public void communicate(Bitmap data) {
        bm = data;
    }
}