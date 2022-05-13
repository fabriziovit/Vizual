package com.app.vizual.APIResponse;

import android.util.Log;

import com.app.vizual.Interfaces.ApiListenerInterface;
import com.app.vizual.Interfaces.ApiInterface;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {
    static final String apiUrl = "http://10.0.2.2:8000/api/";
    ApiInterface objRetrofit;

    public ApiService() {
        this.objRetrofit = new Retrofit.Builder()
                .baseUrl(apiUrl)
                .client(createClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiInterface.class);
    }

    public ApiService(String url) {
        this.objRetrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(createClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiInterface.class);
    }

    private OkHttpClient createClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(600, TimeUnit.SECONDS)
                .readTimeout(600, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .header("Connection", "close")
                            .header("Cache-Control", "no-cache")
                            .build();
                    return chain.proceed(request);
                })
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .retryOnConnectionFailure(true)
                .build();
    }

    public <T> void callRetrofit(Call<T> call, final ApiListenerInterface<T> listener) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(@NotNull Call<T> call, @NotNull retrofit2.Response<T> response) {
                if (response.isSuccessful())
                    listener.getResult(response.body());
                else
                    listener.getResult(null);
            }

            @Override
            public void onFailure(@NotNull Call<T> call, @NotNull Throwable t) {
                Log.d("[DEBUG] APIService", t.getMessage());
                listener.getResult(null);
            }
        });
    }

    public ApiInterface getObjRetrofit() {
        return objRetrofit;
    }
}
