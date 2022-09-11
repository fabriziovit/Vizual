package com.app.vizual;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
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
import com.app.vizual.databinding.ActivityCroppedImageViewBinding;
import com.app.vizual.Fragment.CropFragment;
import com.app.vizual.Fragment.ZoomFragment;

import okhttp3.ResponseBody;
import retrofit2.Call;

//copiare image view ma con left top width ed height che ad ogni chiamta le viene sommata le vecchie coords
public class CroppedImageViewActivity extends AppCompatActivity implements FragmentToActivity {
    private ApiService apiService = new ApiService();
    private ActivityCroppedImageViewBinding binding;
    private int left, top, width, height;
    private String nameImage;
    private Bitmap bmp;
    private boolean isFABOpen = false;
    private FragmentManager fragmentManager;

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

            new Handler(Looper.getMainLooper()).post(() -> {
                Call<ResponseBody> call = apiService.getObjRetrofit().getImageCropped(nameImage, left, top, width, height);
                new Handler(Looper.getMainLooper()).post(() -> {
                    apiService.callRetrofit(call, response -> {
                        if (response == null) {
                            Log.d("DEBUG", "response null");
                            return;
                        }

                        binding.progressBar.setVisibility(View.VISIBLE);
                        bmp = BitmapFactory.decodeStream(response.byteStream());
                        System.out.println(ImageViewActivity.ratio);
                        replaceFragment(new ZoomFragment(bmp, nameImage));
                        binding.progressBar.setVisibility(View.GONE);
                        binding.textView.setVisibility(View.GONE);
                    });
                });
            });
        }

        binding.fabHome.setOnClickListener(view -> {
            Intent intent = new Intent(CroppedImageViewActivity.this, HomePageActivity.class);
            startActivity(intent);
            finish();
        });


        //click yo open the floating button menu
        binding.fabLogo.setOnClickListener(view -> {
            if (!isFABOpen) {
                showFABMenu();
            } else {
                closeFABMenu();
            }
        });

        //click to open fragment for the crop of the image
        binding.fabCrop.setOnClickListener(view -> {
            replaceFragment(new CropFragment(bmp, nameImage, true));
        });

        //click to open fragment for the zoom of the image(the fragment starts when this activity is created)
        binding.fabZoom.setOnClickListener(view -> {
            replaceFragment(new ZoomFragment(bmp, nameImage));
        });

        binding.fabHome.setOnClickListener(view -> {
            Intent intent = new Intent(CroppedImageViewActivity.this, HomePageActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void showFABMenu() {
        isFABOpen = true;
        binding.fabZoom.setVisibility(View.VISIBLE);
        binding.fabCrop.setVisibility(View.VISIBLE);
        binding.fabHome.setVisibility(View.VISIBLE);
        binding.fabZoom.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        binding.fabCrop.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
        binding.fabHome.animate().translationY(-getResources().getDimension(R.dimen.standard_145));
    }

    private void closeFABMenu() {
        isFABOpen = false;
        binding.fabZoom.animate().translationY(0);
        binding.fabCrop.animate().translationY(0);
        binding.fabHome.animate().translationY(0);
        binding.fabZoom.setVisibility(View.GONE);
        binding.fabCrop.setVisibility(View.GONE);
        binding.fabHome.setVisibility(View.GONE);
    }

    private void replaceFragment(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(binding.frameContainer.getId(), fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void communicate(Bitmap data) {
        bmp = data;
    }
}