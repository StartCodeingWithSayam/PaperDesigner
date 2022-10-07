package com.sayam.papperdesigners;

import static com.sayam.papperdesigners.R.id.school;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

public class MainSelector extends AppCompatActivity {

    AppCompatButton school,organization;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_selector);
        getActionBar().hide();
        school = findViewById(R.id.school);
        school.setOnClickListener(view -> {
            startActivity(new Intent(this,StudentScanner.class));
            finish();
        });
        findViewById(R.id.organization).setOnClickListener(view -> {
            startActivity(new Intent(this, TeacherLoginActivity.class));
            finish();
        });

    }
}