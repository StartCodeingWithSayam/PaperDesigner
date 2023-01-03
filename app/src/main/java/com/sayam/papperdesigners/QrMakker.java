package com.sayam.papperdesigners;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.io.File;
import java.io.FileOutputStream;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QrMakker extends AppCompatActivity {
    Button generate, share;
    EditText link;
    ImageView qrImage;
    QRGEncoder encoder;
    AdView qrAdview;
    Bitmap bitmap;
    InterstitialAd generateInterAd, shareInterAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_makker);
        link = findViewById(R.id.et_link);
        qrImage = findViewById(R.id.imgQr);
        VectorDrawable vectorDrawable = (VectorDrawable) qrImage.getDrawable();
        Bitmap b = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(b);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), b);
        bitmap = bitmapDrawable.getBitmap();
        qrAdview = findViewById(R.id.qrAdView);
        AdRequest request = new AdRequest.Builder().build();
        qrAdview.loadAd(request);
        generate = findViewById(R.id.link_btn);
        share = findViewById(R.id.btn_share);
        generate.setOnClickListener(v -> {
            if (generateInterAd != null) generateInterAd.show(this);
            else generateQr();
        });
        share.setOnClickListener(v -> {
            if (shareInterAd == null) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    shareImage(bitmap);
                } else {
                    askPermission();
                }
            } else shareInterAd.show(this);
        });


        // Inter Ad for making qrCode
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", request, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                generateInterAd = null;
                Log.d(MainActivity.TAG, "onAdFailedToLoad: Error: " + loadAdError.getMessage());
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                generateInterAd = interstitialAd;
                generateInterAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        generateQr();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        Log.d(MainActivity.TAG, "onAdFailedToShowFullScreenContent: Error to show to full screen");
                        generateInterAd = null;
                    }
                });
            }
        });
        // share InterAd
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", request, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                shareInterAd = null;
                Log.d(MainActivity.TAG, "onAdFailedToLoad: Error: " + loadAdError.getMessage());
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                shareInterAd = interstitialAd;
                shareInterAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        if (ContextCompat.checkSelfPermission(QrMakker.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            shareImage(bitmap);
                        } else {
                            askPermission();
                        }
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        Log.d(MainActivity.TAG, "onAdFailedToShowFullScreenContent: Error to show to full screen");
                        shareInterAd = null;
                    }
                });
            }
        });

    }

    private void generateQr() {
        String data = link.getText().toString();
        if (data.isEmpty()) {
            Toast.makeText(this, "Enter a url please", Toast.LENGTH_SHORT).show();
        } else {
            encoder = new QRGEncoder(data, null, QRGContents.Type.TEXT, 500);
            try {
                Bitmap qrBitmap = encoder.getBitmap();
                qrImage.setImageBitmap(qrBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    void shareImage(Bitmap bitmap) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        File file = new File(getExternalCacheDir() + "/" + " QrCode" + ".png");
        Intent intent;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
            fileOutputStream.flush();
            intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        startActivity(Intent.createChooser(intent, "Share Image Via: "));
    }
}