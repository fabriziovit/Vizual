package com.app.vizual.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.app.vizual.Presenters.SplashScreenPresenter;
import com.app.vizual.databinding.ActivitySplashScreenBinding;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreenPresenter splashScreenPresenter = new SplashScreenPresenter(this);
        ActivitySplashScreenBinding binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        splashScreenPresenter.Transition(binding);
    }

    public Activity getStartActivity(){
        return this;
    }
}