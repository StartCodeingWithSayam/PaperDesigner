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
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class ClientPageActivity extends AppCompatActivity {
     AppCompatButton business, costumer;
     InterstitialAd businessInterAd,costumerInterAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_page);
        AdRequest request = new AdRequest.Builder().build();
        init();
        business.setOnClickListener(view -> {
            startActivity(new Intent(this, BusinessLoginPageActivity.class));
            finish();
        });
        costumer.setOnClickListener(view -> {
            startActivity(new Intent(this,StudentScanner.class));
            finish();
        });

        InterstitialAd.load(this, getString(R.string.INTER_AD_UNIT_ID), request, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                businessInterAd = null;
                Log.d(MainActivity.TAG, "onAdFailedToLoad: Error " + loadAdError.getMessage());
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                businessInterAd = interstitialAd;
                businessInterAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        Log.d(MainActivity.TAG, "onAdDismissedFullScreenContent: Dismissed ");
                        startActivity(new Intent(ClientPageActivity.this, BusinessLoginPageActivity.class));
                        finish();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        Log.d(MainActivity.TAG, "onAdFailedToLoad: Error " + adError.getMessage());
                        businessInterAd = null;
                    }
                });
            }
        });

        InterstitialAd.load(this, getString(R.string.INTER_AD_UNIT_ID), request, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                costumerInterAd = null;
                Log.d(MainActivity.TAG, "onAdFailedToLoad: Error " + loadAdError.getMessage());
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                costumerInterAd = interstitialAd;
                costumerInterAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        Log.d(MainActivity.TAG, "onAdDismissedFullScreenContent: Dismissed ");
                        startActivity(new Intent(ClientPageActivity.this, TeacherLoginActivity.class));
                        finish();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        Log.d(MainActivity.TAG, "onAdFailedToLoad: Error " + adError.getMessage());
                        costumerInterAd = null;
                    }
                });
            }
        });

    }
    private void init(){
        business = findViewById(R.id.business);
        costumer = findViewById(R.id.consumer);
    }
}