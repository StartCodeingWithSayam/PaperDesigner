package com.sayam.papperdesigners;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QrMakker extends AppCompatActivity {
    Button generate, share;
    EditText link;
    ImageView qrImage;
    QRGEncoder encoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_makker);
        link = findViewById(R.id.et_link);
        qrImage = findViewById(R.id.imgQr);
        generate = findViewById(R.id.link_btn);
        share = findViewById(R.id.btn_share);
        generate.setOnClickListener(v -> {
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
        });
        share.setOnClickListener(v->{
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                saveImage();
            }else{
                askPermission();
            }
        });
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    private void saveImage() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) qrImage.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        Uri uri = getImageToShare(bitmap);
        Intent i = new Intent(Intent.ACTION_SEND);
        i.putExtra(Intent.EXTRA_STREAM,uri);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setType("image/*");
        startActivity(Intent.createChooser(i,"Share Qr Code"));
    }

    private Uri getImageToShare(@NonNull Bitmap bitmap) {
        File f = new File(getCacheDir(),"images");
        Uri u;
        try {
            f.mkdir();
            File file = new File(f,"shared_image.jpeg");
            FileOutputStream outputStream =  new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            Toast.makeText(this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
        u = FileProvider.getUriForFile(this,"com.sayam.papperdesigners",f);
        return u;
    }
}