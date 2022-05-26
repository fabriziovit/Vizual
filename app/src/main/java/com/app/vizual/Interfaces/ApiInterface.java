package com.app.vizual.Interfaces;

import com.app.vizual.Models.ListImages;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface ApiInterface {
    @GET("get-images")
    Call<ListImages> getImages();

    @Streaming
    @FormUrlEncoded
    @POST("get-image")
    Call<ResponseBody> getImage(@Field("name") String name);
}