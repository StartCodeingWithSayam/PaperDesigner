package com.sayam.papperdesigners;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class QuesPaperMaker extends AppCompatActivity {
    WebView view;
    
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ques_paper_maker);
        view = findViewById(R.id.webView);
        view.loadUrl("https://docs.google.com/forms/u/0/");
        WebSettings settings = view.getSettings();
        settings.setJavaScriptEnabled(true);
        view.setWebViewClient(new WebViewClient());
    }

    @Override
    public void onBackPressed() {
        if (view.canGoBack())
            view.goBack();
        super.onBackPressed();
    }
}