package com.sayam.papperdesigners;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AnswerPaper extends AppCompatActivity {
    WebView view;
    String url;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_paper);
        Intent intent = getIntent();
        url = intent.getStringExtra(StudentScanner.MSG);
        view = findViewById(R.id.view_web);
        view.loadUrl(url);
        view.setWebViewClient(new WebViewClient());
        WebSettings settings = view.getSettings();
        settings.setJavaScriptEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if (view.canGoBack())
            view.goBack();
        super.onBackPressed();
    }
}