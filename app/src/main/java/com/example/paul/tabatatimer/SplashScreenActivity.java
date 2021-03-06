package com.example.paul.tabatatimer;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EasySplashScreen config = new EasySplashScreen(SplashScreenActivity.this)
                .withFullScreen()
                .withTargetActivity(MainActivity.class)
                .withSplashTimeOut(2000)
                .withBackgroundColor(Color.WHITE)
                .withLogo(R.drawable.logo)
                .withHeaderText("Developed by Paul Kolomeitsev")
                .withFooterText("Tabata Timer");
        config.getHeaderTextView().setTextColor(Color.BLACK);
        config.getHeaderTextView().setTextSize(14);
        config.getFooterTextView().setTextColor(Color.BLACK);
        config.getFooterTextView().setTextSize(28);

        View easySplashScreen = config.create();
        setContentView(easySplashScreen);
    }
}
