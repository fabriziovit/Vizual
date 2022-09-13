package com.app.vizual.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.app.vizual.Presenters.SplashScreenPresenter;
import com.app.vizual.databinding.ActivitySplashScreenBinding;


public class SplashScreenActivity extends AppCompatActivity {
    private ActivitySplashScreenBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreenPresenter splashScreenPresenter = new SplashScreenPresenter(this);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        splashScreenPresenter.transition(binding);
    }

    public Activity getStartActivity(){
        return this;
    }
}