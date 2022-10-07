package com.sayam.papperdesigners;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;

public class ClientPageActivity extends AppCompatActivity {
     AppCompatButton business, costumer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_page);
        getActionBar().hide();
        init();
        business.setOnClickListener(view -> {
            startActivity(new Intent(this, BusinessLoginPageActivity.class));
            finish();
        });
        costumer.setOnClickListener(view -> {
            startActivity(new Intent(this,StudentScanner.class));
            finish();
        });
    }
    private void init(){
        business = findViewById(R.id.business);
        costumer = findViewById(R.id.consumer);
    }
}