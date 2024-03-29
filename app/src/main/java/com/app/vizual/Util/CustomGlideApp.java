package com.app.vizual.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.vizual.APIResponse.ApiService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.InputStream;

import okhttp3.Call;

@GlideModule
public class CustomGlideApp extends AppGlideModule {

    //get bitmap and load it into the subsampling image view
    public void init(Context context, Bitmap bitmap, SubsamplingScaleImageView subsamplingScaleImageView){
        Glide.with(context).asBitmap().load(bitmap).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition transition) {
                subsamplingScaleImageView.setImage(ImageSource.cachedBitmap(resource));
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {}
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
