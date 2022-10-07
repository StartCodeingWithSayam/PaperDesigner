package com.sayam.papperdesigners;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SelectorActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);
        findViewById(R.id.student).setOnClickListener(view -> {
            startActivity(new Intent(this,StudentScanner.class));
            finish();
        });
        findViewById(R.id.teacher).setOnClickListener(view -> {
            startActivity(new Intent(this, TeacherLoginActivity.class));
            finish();
        });
    }
}