package com.app.vizual;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import com.app.vizual.APIResponse.ApiService;

import androidx.appcompat.app.AppCompatActivity;

import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.vizual.databinding.ActivityImageViewBinding;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class ImageViewActivity extends AppCompatActivity {
    private ActivityImageViewBinding binding;
    ApiService apiService = new ApiService();
    String currentSelection;
    boolean fabClicked = false;

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

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view = (DragRectView) binding.dragRect;
                if (!fabClicked) {
                    fabClicked = true;
                    binding.dragRect.setVisibility(View.VISIBLE);
                    ((DragRectView) view).setOnUpCallback(new DragRectView.OnUpCallback() {
                        @Override
                        public void onRectFinished(final Rect rect) {
                            Toast.makeText(getApplicationContext(), "Rect is (" + rect.left + ", " + rect.top + ", " + rect.right + ", " + rect.bottom + ")",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    binding.dragRect.setVisibility(View.GONE);
                    fabClicked = false;
                    // invio per avere parte ritagliata più dettagliata calcolando la posizione del drag all'interno dell'immagine
                }
            }
        });
    }

    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
            }
            Log.d("DEBUG", "width : " + finalWidth);
            Log.d("DEBUG", "height : " + finalHeight);
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }
}