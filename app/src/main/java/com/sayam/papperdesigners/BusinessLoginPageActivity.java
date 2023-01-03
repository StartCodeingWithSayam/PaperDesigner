package com.sayam.papperdesigners;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class BusinessLoginPageActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 23;
    EditText email_bs,password_bs;
    TextView makeAccount_bs,reset_bs;
    Button login_bs,google_bs;
    GoogleSignInClient client_bs;
    FirebaseAuth mAuth;
    FirebaseDatabase database_bs;
    ProgressDialog dialog;
    AdView loginAdView;
    InterstitialAd loginInterAd,googleInterAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bussnes_login_page);
        init();
        AdRequest request = new AdRequest.Builder().build();
        loginAdView.loadAd(request);
        createRequest();
        makeAccount_bs.setOnClickListener(view -> {
            startActivity(new Intent(this, TeacherSignInActivity.class));
            finish();
        });
        login_bs.setOnClickListener(v->{
            if (loginInterAd !=null)
                loginInterAd.show(this);
            else{
                dialog.show();
                LogInWithEmailPassword();
            }
        });
        google_bs.setOnClickListener(v-> {
            if (googleInterAd !=null) {
                googleInterAd.show(this);
            }
            else
                signIn();
        });
        reset_bs.setOnClickListener(v->startActivity(new Intent(this,ForgotPassword.class)));

        // Login InterAd
        InterstitialAd.load(this, getString(R.string.INTER_AD_UNIT_ID), request, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.d(MainActivity.TAG, "onAdFailedToLoad: Error: "+loadAdError.getMessage());
                loginInterAd = null;
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                loginInterAd = interstitialAd;
                loginInterAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        Log.d(MainActivity.TAG, "onAdDismissedFullScreenContent: Closed");
                        dialog.show();
                        LogInWithEmailPassword();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        loginInterAd = null;
                        Log.d(MainActivity.TAG, "onAdFailedToShowFullScreenContent: Error: "+adError.getMessage());
                    }
                });
            }
        });


        // Google InterAd

        InterstitialAd.load(this, getString(R.string.INTER_AD_UNIT_ID), request, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.d(MainActivity.TAG, "onAdFailedToLoad: Error: "+loadAdError.getMessage());
                googleInterAd = null;
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                googleInterAd = interstitialAd;
                googleInterAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        Log.d(MainActivity.TAG,"onAdDismissedFullScreenContent : Dismissed");
                        signIn();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        googleInterAd = null;
                        Log.d(MainActivity.TAG, "onAdFailedToShowFullScreenContent: Error: "+adError.getMessage());
                    }
                });
            }
        });

    }

    private void LogInWithEmailPassword(){
        mAuth.signInWithEmailAndPassword(email_bs.getText().toString(),password_bs.getText().toString())
                .addOnCompleteListener(task -> {
                    dialog.dismiss();
                    if (!task.isSuccessful()){
                        Toast.makeText(this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }else{
                        FirebaseUser users = mAuth.getCurrentUser();
                        UserModel userModel = new UserModel();
                        assert users != null;
                        userModel.setUserId(users.getUid());
                        userModel.setUserName(users.getDisplayName());
                        userModel.setUserEmail(users.getEmail());
                        database_bs.getReference().child("Organization")
                                .child(users.getUid()).setValue(userModel);
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    }
                });
    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(@NonNull GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task-> {
                    if (task.isSuccessful()) {
                        FirebaseUser users = mAuth.getCurrentUser();
                        UserModel userModel = new UserModel();
                        assert users != null;
                        userModel.setUserId(users.getUid());
                        userModel.setUserName(users.getDisplayName());
                        userModel.setUserEmail(users.getEmail());
                        database_bs.getReference().child("Organization")
                                .child(users.getUid()).setValue(userModel);
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    }
                });

    }
    @SuppressWarnings("deprecation")
    private void signIn(){
        Intent i = client_bs.getSignInIntent();
        startActivityForResult(i,RC_SIGN_IN);
    }
    private void createRequest(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        client_bs = GoogleSignIn.getClient(this,gso);
    }
    private void init(){
        loginAdView = findViewById(R.id.LoginAdView);
        email_bs = findViewById(R.id.et_emailId_bss);
        password_bs = findViewById(R.id.et_passwordId_bss);
        makeAccount_bs = findViewById(R.id.make_account_bs);
        reset_bs = findViewById(R.id.tv_reset_password_bss);
        login_bs = findViewById(R.id.btn_login_bss);
        mAuth = FirebaseAuth.getInstance();
        database_bs = FirebaseDatabase.getInstance();
        google_bs = findViewById(R.id.google_logIn_bs);
        dialog = new ProgressDialog(this);
        dialog.setTitle("LogIn...");
        dialog.setMessage("Logging you in...");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this,MainActivity.class));
        }
    }
}