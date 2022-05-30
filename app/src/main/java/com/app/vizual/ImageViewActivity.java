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

import android.view.View;

import com.app.vizual.Interfaces.FragmentToActivity;
import com.app.vizual.databinding.ActivityImageViewBinding;
import com.app.vizual.fragment.CropFragment;
import com.app.vizual.fragment.ZoomFragment;

public class ImageViewActivity extends AppCompatActivity implements FragmentToActivity {
    private ActivityImageViewBinding binding;
    ApiService apiService = new ApiService();
    String currentSelection;
    //boolean fabClicked = false;
    boolean isFABOpen = false;
    boolean isCropDisabled = false;
    private FragmentManager fragmentManager;
    Bitmap bm;

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

        /*
        // Call API
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
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
                Bitmap bmp = BitmapFactory.decodeStream(response.byteStream());
                //immagine ridimensiionata: I/System.out: 1250 1503  max width: 2560 max height: 1504 dim immagine originale width: 32001 height: 38474 ratio 0.03909133440765192
                //trovare ratio o farselo inviare dal server?! moltiplicare le coordinate del crap per il ratio e ritornarle al server che dovra poi rispedire solo la parte scelta
                // dopo la prima conversione salvare le immagini in locale e poi cancellarle una volta spento il server o ...
                binding.imageView.setImageBitmap(bmp);
                binding.progressBar.setVisibility(View.GONE);
            });
        }
         */
        bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.test), 1250, 1504, false);

        replaceFragment(new ZoomFragment(bm));
        /*
        binding.imageView.setImageBitmap(bm);
        */
        binding.progressBar.setVisibility(View.GONE);
        binding.textView.setVisibility(View.GONE);


        //bottone per aprire il menu dei floating button
        binding.fabLogo.setOnClickListener(view -> {
            if (!isFABOpen) {
                showFABMenu();
            } else {
                closeFABMenu();
                //binding.imageView.setShowCropOverlay(false);
                isCropDisabled = true;
            }
        });

        //click bottone per il ritaglio dell'immagine
        binding.fabCrop.setOnClickListener(view -> {
            /*if(!fabClicked){
                fabClicked = true;
                binding.imageView.setShowCropOverlay(true);
            }else {
                if (!isCropDisabled){
                    binding.imageView.setShowCropOverlay(false);
                    //trovo i punti x,y del crop
                    System.out.println(Arrays.toString(binding.imageView.getCropPoints()));
                    //trovo x e y e la width e la height
                    System.out.println("AAAAAAAA    " + binding.imageView.getCropRect().left + ", " + binding.imageView.getCropRect().top + ", " + binding.imageView.getCropRect().width() + ", " + binding.imageView.getCropRect().height());

                    //prendere questo per i punti all'interno del bitmap
                    System.out.println("EEEEEEEE    " + binding.imageView.getCropRect().left + ", " + binding.imageView.getCropRect().top + ", " + binding.imageView.getCropRect().right + ", " + binding.imageView.getCropRect().bottom);

                    System.out.println("BBBBBBBB    " + binding.imageView.getWholeImageRect().left + ", " + binding.imageView.getWholeImageRect().top + ", " + binding.imageView.getWholeImageRect().right + ", " + binding.imageView.getWholeImageRect().bottom);
                    System.out.println("CCCCCCCC    " + binding.imageView.getCropWindowRect().left + ", " + binding.imageView.getCropWindowRect().top + ", " + binding.imageView.getCropWindowRect().width() + ", " + binding.imageView.getCropWindowRect().height());
                    binding.imageView.setImageBitmap(binding.imageView.getCroppedImage());
                    /* OUTPUT SE SI USA IL CROP SU TUTTO IL BITMAP
                    I/System.out: [124.99999, 150.40001, 1125.0, 150.40001, 1125.0, 1353.5999, 124.99999, 1353.5999]  RECTANGLE
                    I/System.out: AAAAAAAA    125, 150, 1000, 1204
                    I/System.out: BBBBBBBB    0, 0, 1250, 1504
                    I/System.out: CCCCCCCC    121.0, 145.65123, 968.0, 1164.6975

                    fabClicked = false;
                }else
                    binding.imageView.setShowCropOverlay(true);
                isCropDisabled = false;
            }*/
            replaceFragment(new CropFragment(bm));

        });

        //mettere il bitmap in un anuova activity? in modo che l'image view possa essere zommata?
        binding.fabZoom.setOnClickListener(view -> {
            //ByteArrayOutputStream stream = new ByteArrayOutputStream();
            //bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            //byte[] byteArray = stream.toByteArray();
            replaceFragment(new ZoomFragment(bm));
            //Intent intent = new Intent(ImageViewActivity.this, ZoomImageView.class);
            //intent.putExtra("image", byteArray);
            //startActivity(intent);
        });

        binding.fabHome.setOnClickListener(view -> {
            Intent intent = new Intent(ImageViewActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void showFABMenu(){
        isFABOpen=true;
        binding.fabZoom.setVisibility(View.VISIBLE);
        binding.fabCrop.setVisibility(View.VISIBLE);
        binding.fabHome.setVisibility(View.VISIBLE);
        binding.fabZoom.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        binding.fabCrop.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
        binding.fabHome.animate().translationY(-getResources().getDimension(R.dimen.standard_145));
    }

    private void closeFABMenu(){
        isFABOpen=false;
        binding.fabZoom.animate().translationY(0);
        binding.fabCrop.animate().translationY(0);
        binding.fabHome.animate().translationY(0);
        binding.fabZoom.setVisibility(View.GONE);
        binding.fabCrop.setVisibility(View.GONE);
        binding.fabHome.setVisibility(View.GONE);
    }

    private void replaceFragment(Fragment fragment){
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