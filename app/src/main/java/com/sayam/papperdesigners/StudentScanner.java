package com.sayam.papperdesigners;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class StudentScanner extends AppCompatActivity {
    public static String  MSG = "com.sayam.papperdesigners.MSG";
    CodeScannerView scannerView;
    CodeScanner scanner;
    Button openLink;
    TextView resultData;
    LinearLayout ll;
    String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_scanner);
        init();
        scanner.setDecodeCallback(r-> {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    resultData.setText(r.toString());
                    result = resultData.getText().toString();
                }
            });
        });

        openLink.setOnClickListener(view -> {
            Intent i = new Intent(this,AnswerPaper.class);
            i.putExtra(MSG,result);
            startActivity(i);
            finish();
        });
        scannerView.setOnClickListener(v->{
            scanner.startPreview();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestForCamara();
    }

    private void requestForCamara(){
        Dexter.withContext(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        scanner.startPreview();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(StudentScanner.this, "Camara permission is required", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }
    private void init(){
        openLink = findViewById(R.id.link_btn);
        scannerView = findViewById(R.id.scanner_view);
        scanner = new CodeScanner(this,scannerView);
        resultData = findViewById(R.id.result);
    }
}