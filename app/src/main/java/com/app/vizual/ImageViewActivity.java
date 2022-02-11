package com.app.vizual;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import com.app.vizual.APIResponse.ApiService;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final DragRectView view;

        binding = ActivityImageViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        //GetExtra per prendere il nome dell'immagine ed eseguire le API
        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("currentSelection") != null) {
            currentSelection = bundle.getString("currentSelection");
        }

        // Call API
        Call<ResponseBody> call = apiService.getObjRetrofit().getImage(currentSelection);
        apiService.callRetrofit(call, response -> {
            if (response == null) {
                Log.d("DEBUG", "response null");
                return;
            }
            binding.progressBar.setVisibility(View.VISIBLE);
            Bitmap bmp = BitmapFactory.decodeStream(response.byteStream());
            bmp.getWidth();
            bmp.getHeight();

            Bitmap scaledBitmap = resize(bmp, width, height);
            binding.progressBar.setVisibility(View.GONE);
            binding.imageView.setImageBitmap(scaledBitmap);
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 view = (DragRectView) binding.dragRect;

                if (null != view) {
                    View finalView = view;
                    ((DragRectView) view).setOnUpCallback(new DragRectView.OnUpCallback() {
                        @Override
                        public void onRectFinished(final Rect rect) {
                            Toast.makeText(getApplicationContext(), "Rect is (" + rect.left + ", " + rect.top + ", " + rect.right + ", " + rect.bottom + ")",
                                    Toast.LENGTH_LONG).show();
                            ((DragRectView) finalView).cancelDragAndDrop();
                        }
                    });
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
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            Log.d("DEBUG", "width : "+ finalWidth);
            Log.d("DEBUG", "height : "+ finalHeight);
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }
}