package com.app.vizual.Interfaces;

import com.app.vizual.Models.IntegerModel;
import com.app.vizual.Models.ListImages;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface ApiInterface {
    @GET("get-images")
    Call<ListImages> getImages();

    @Streaming
    @FormUrlEncoded
    @POST("get-image")
    Call<ResponseBody> getImage(@Field("name") String name);

    @Streaming
    @GET("get-image-cropped/{name}/{left}_{top}_{width}x{height}")
    Call<ResponseBody> getImageCropped(@Path("name")String name, @Path("left")int left, @Path("top") int top, @Path("width")int width, @Path("height")int height);

    @GET("get-image-width/{name}")
    Call<IntegerModel> getWidth(@Path("name")String name);

    @GET("get-image-height/{name}")
    Call<IntegerModel> getHeight(@Path("name")String name);
}