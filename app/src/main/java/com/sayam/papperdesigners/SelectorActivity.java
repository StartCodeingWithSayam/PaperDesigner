package com.sayam.papperdesigners;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class SelectorActivity extends AppCompatActivity {
    AppCompatButton teacher,student;
    InterstitialAd studentInterAd,teacherInterAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);
        AdRequest request = new AdRequest.Builder().build();
        student = findViewById(R.id.student);
        teacher=findViewById(R.id.teacher);
        student.setOnClickListener(view -> {
            if (studentInterAd == null){
                startActivity(new Intent(SelectorActivity.this, StudentScanner.class));
                finish();
            }else
                studentInterAd.show(this);
        });
        teacher.setOnClickListener(view -> {
            if (studentInterAd == null){
                startActivity(new Intent(SelectorActivity.this, TeacherLoginActivity.class));
                finish();
            }else
                studentInterAd.show(this);
        });
        // teacher InterAd
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", request, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.d(MainActivity.TAG, "onAdFailedToLoad: Error: "+loadAdError.getMessage());
                studentInterAd = null;
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                studentInterAd = interstitialAd;
                studentInterAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        Log.d(MainActivity.TAG,"onAdDismissedFullScreenContent : Dismissed");
                        startActivity(new Intent(SelectorActivity.this,StudentScanner.class));
                        finish();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        studentInterAd = null;
                        Log.d(MainActivity.TAG, "onAdFailedToShowFullScreenContent: Error: "+adError.getMessage());
                    }
                });
            }
        });


        // Student InterAd
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", request, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.d(MainActivity.TAG, "onAdFailedToLoad: Error: "+loadAdError.getMessage());
                teacherInterAd = null;
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                teacherInterAd = interstitialAd;
                teacherInterAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        Log.d(MainActivity.TAG,"onAdDismissedFullScreenContent : Dismissed");
                        startActivity(new Intent(SelectorActivity.this, TeacherLoginActivity.class));
                        finish();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        teacherInterAd = null;
                        Log.d(MainActivity.TAG, "onAdFailedToShowFullScreenContent: Error: "+adError.getMessage());
                    }
                });
            }
        });


    }
}