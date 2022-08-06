package com.sayam.papperdesigners;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.btn_create).setOnClickListener(v-> {
            startActivity(new Intent(MainActivity.this,QuesPaperMaker.class));
            finish();
        });
        findViewById(R.id.btn_generate).setOnClickListener(v-> {
            startActivity(new Intent(MainActivity.this,QrMakker.class));
            finish();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout){
            auth.signOut();
            startActivity(new Intent(MainActivity.this,SelectorActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}