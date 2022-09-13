package com.app.vizual.Presenters;

import android.content.Intent;

import androidx.constraintlayout.motion.widget.MotionLayout;

import com.app.vizual.Views.HomePageActivity;
import com.app.vizual.Views.SplashScreenActivity;
import com.app.vizual.databinding.ActivitySplashScreenBinding;

public class SplashScreenPresenter {
    private final SplashScreenActivity splashScreenActivity;

    public SplashScreenPresenter(SplashScreenActivity splashScreenActivity) {
        this.splashScreenActivity = splashScreenActivity;
    }

    //once the trnasition is done open the main activity to choose the image
    public void transition(ActivitySplashScreenBinding binding){
        binding.motionLayout.addTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {
            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {
            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int i) {
                Intent intent = new Intent(splashScreenActivity, HomePageActivity.class);
                splashScreenActivity.getStartActivity().startActivity(intent);
                splashScreenActivity.getStartActivity().finish();
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {
            }
        });
    }
}
