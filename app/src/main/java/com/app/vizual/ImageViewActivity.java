package com.app.vizual;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.app.vizual.APIResponse.ApiService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import com.app.vizual.Interfaces.FragmentToActivity;
import com.app.vizual.databinding.ActivityImageViewBinding;
import com.app.vizual.fragment.CropFragment;
import com.app.vizual.fragment.ZoomFragment;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class ImageViewActivity extends AppCompatActivity implements FragmentToActivity {
    private ActivityImageViewBinding binding;
    private ApiService apiService = new ApiService();
    private String currentSelection;
    private boolean isFABOpen = false;
    private boolean isCropDisabled = false;
    private FragmentManager fragmentManager;
    public static Bitmap originalBitmap;
    private Bitmap bm;
    public static Bitmap grayscaledImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityImageViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                if (response == null) {
                    Log.d("DEBUG", "response null");
                    return;
                }
                binding.progressBar.setVisibility(View.VISIBLE);
                bm = BitmapFactory.decodeStream(response.byteStream());
                originalBitmap = bm;
                replaceFragment(new ZoomFragment(bm, currentSelection));
                binding.progressBar.setVisibility(View.GONE);
                binding.textView.setVisibility(View.GONE);
                Call<ResponseBody> callGray = apiService.getObjRetrofit().getImageGrayscaled(currentSelection);
                apiService.callRetrofit(callGray, responseBody -> {
                    if (responseBody == null) {
                        Log.d("DEBUG", "response null");
                        return;
                    }
                    grayscaledImage = BitmapFactory.decodeStream(responseBody.byteStream());
                });
            });
        }

        //click to open the floating button menu
        binding.fabLogo.setOnClickListener(view -> {
            if (!isFABOpen) {
                showFABMenu();
            } else {
                closeFABMenu();
            }
        });

        //click to open fragment for the crop of the image
        binding.fabCrop.setOnClickListener(view -> {
            replaceFragment(new CropFragment(bm, currentSelection));

        });

        //click to open fragment for the zoom of the image(the fragment starts when this activity is created)
        binding.fabZoom.setOnClickListener(view -> {
            replaceFragment(new ZoomFragment(bm, currentSelection));
        });

        //click to return to selection activity
        binding.fabHome.setOnClickListener(view -> {
            Intent intent = new Intent(ImageViewActivity.this, MainActivity.class);
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
        bm = data;
    }
}