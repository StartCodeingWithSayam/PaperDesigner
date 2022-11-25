package com.sayam.papperdesigners;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class SelectorActivity extends AppCompatActivity {
    Button teacher,student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);
        student = findViewById(R.id.student);
        teacher=findViewById(R.id.teacher);
        student.setOnClickListener(view -> {
            startActivity(new Intent(SelectorActivity.this,StudentScanner.class));
            finish();
        });
        teacher.setOnClickListener(view -> {
            startActivity(new Intent(SelectorActivity.this, TeacherLoginActivity.class));
            finish();
        });
    }
}