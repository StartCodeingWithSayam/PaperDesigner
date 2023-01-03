package com.sayam.papperdesigners;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class MainSelector extends AppCompatActivity {

    AppCompatButton school,organization;
    InterstitialAd schoolInterAd,organizationInterAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AdRequest request = new AdRequest.Builder().build();
        setContentView(R.layout.activity_main_selector);
        MobileAds.initialize(this, initializationStatus -> {
        });
        school = findViewById(R.id.school);
        school.setOnClickListener(view -> {
            if (schoolInterAd == null){
                startActivity(new Intent(this, SelectorActivity.class));
                finish();
            }else
                schoolInterAd.show(this);
        });
        organization = findViewById(R.id.organization);
        organization.setOnClickListener(view -> {
            if (organizationInterAd == null){
                startActivity(new Intent(this, ClientPageActivity.class));
                finish();
            }else{
                organizationInterAd.show(this);
            }
        });
        // school InterAd
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", request, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.d(MainActivity.TAG, "onAdFailedToLoad: Error: "+loadAdError.getMessage());
                schoolInterAd = null;
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                schoolInterAd = interstitialAd;
                schoolInterAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        Log.d(MainActivity.TAG,"onAdDismissedFullScreenContent : Dismissed");
                        startActivity(new Intent(MainSelector.this,SelectorActivity.class));
                        finish();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        schoolInterAd = null;
                        Log.d(MainActivity.TAG, "onAdFailedToShowFullScreenContent: Error: "+adError.getMessage());
                    }
                });
            }
        });
        // organization InterAd
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", request, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.d(MainActivity.TAG, "onAdFailedToLoad: Error: "+loadAdError.getMessage());
                organizationInterAd = null;
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                organizationInterAd = interstitialAd;
                organizationInterAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        Log.d(MainActivity.TAG,"onAdDismissedFullScreenContent : Dismissed");
                        startActivity(new Intent(MainSelector.this,ClientPageActivity.class));
                        finish();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        organizationInterAd = null;
                        Log.d(MainActivity.TAG, "onAdFailedToShowFullScreenContent: Error: "+adError.getMessage());
                    }
                });
            }
        });

    }
}