package com.sayam.papperdesigners;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class BusinessSigningActivity extends AppCompatActivity {
    EditText name,email,password;
    Button register;
    TextView alreadyHaveAccount;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog dialog;
    String result;
    AdView signInAdView;
    InterstitialAd signInInterAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bussness_signin);
        init();
        AdRequest request = new AdRequest.Builder().build();
        signInAdView.loadAd(request);
        result = password.getText().toString();
        Encrypt(result);
        alreadyHaveAccount.setOnClickListener(view -> startActivity(new Intent(this, TeacherLoginActivity.class)));
        register.setOnClickListener(v->{
            if (signInInterAd !=null)
                signInInterAd.show(this);
            else {
                dialog.show();
                SignInWithEmailPassword();
            }
        });
        // Register InterAd
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", request, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.d(MainActivity.TAG, "onAdFailedToLoad: Error: "+loadAdError.getMessage());
                signInInterAd = null;
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                 signInInterAd= interstitialAd;
                signInInterAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        Log.d(MainActivity.TAG, "onAdDismissedFullScreenContent: Closed");
                        dialog.show();
                        SignInWithEmailPassword();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        signInInterAd = null;
                        Log.d(MainActivity.TAG, "onAdFailedToShowFullScreenContent: Error: "+adError.getMessage());
                    }
                });
            }
        });

    }

    private void SignInWithEmailPassword(){
        auth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(task -> {
            dialog.dismiss();
            if (task.isSuccessful()){
                UserModel userModel = new UserModel(
                        name.getText().toString(),email.getText().toString(),result
                );
                String id = Objects.requireNonNull(task.getResult().getUser()).getUid();
                database.getReference().child("Organization").child(id).setValue(userModel);
                Toast.makeText(this, "Account SignUp Successfully", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, Objects.requireNonNull(task.getException()).toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void Encrypt(String data){
        try{
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(data.getBytes());
            byte[] d = digest.digest();
            StringBuilder MD5Hash = new StringBuilder();
            for (int i=0;i<MD5Hash.length();i++){
                String h = Integer.toHexString(0xff & d[i]);
                while(h.length()<2){
                    h = "0"+h;
                    MD5Hash.append(h);
                }
                result = MD5Hash.toString();
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    private void init(){
        signInAdView = findViewById(R.id.TeacherSignInAdView);
        name = findViewById(R.id.et_name_bs);
        email = findViewById(R.id.et_emailId_bs);
        password = findViewById(R.id.et_passwordId_bs);
        register = findViewById(R.id.btn_register_bs);
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setTitle("SignUp");
        dialog.setMessage("We are SignIn you in..");
        database = FirebaseDatabase.getInstance();
        alreadyHaveAccount = findViewById(R.id.already_have_account_bs);

    }
}