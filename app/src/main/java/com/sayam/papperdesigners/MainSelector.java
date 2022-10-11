package com.sayam.papperdesigners;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.content.Intent;
import android.os.Bundle;

public class MainSelector extends AppCompatActivity {

    AppCompatButton school,organization;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_selector);
        school = findViewById(R.id.school);
        school.setOnClickListener(view -> {
            startActivity(new Intent(this,SelectorActivity.class));
            finish();
        });
        organization = findViewById(R.id.organization);
        organization.setOnClickListener(view -> {
            startActivity(new Intent(this, ClientPageActivity.class));
            finish();
        });
    }
}