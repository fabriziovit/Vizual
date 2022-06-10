package com.app.vizual;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.vizual.APIResponse.ApiService;
import com.app.vizual.fragment.ZoomFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.InputStream;
import java.lang.annotation.Target;

import okhttp3.Call;

@GlideModule
public class CustomGlideApp extends AppGlideModule {

    public void init(Fragment fragment, Bitmap bitmap, SubsamplingScaleImageView subsamplingScaleImageView){
        Glide.with(fragment).asBitmap().load(bitmap).into(new CustomTarget() {
            @Override
            public void onResourceReady(@NonNull Object resource, @Nullable Transition transition) {
                subsamplingScaleImageView.setImage(ImageSource.cachedBitmap((Bitmap) resource));
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {            }
        });
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        glide.getRegistry().replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory((Call.Factory) ApiService.createClient()));
    }
}
