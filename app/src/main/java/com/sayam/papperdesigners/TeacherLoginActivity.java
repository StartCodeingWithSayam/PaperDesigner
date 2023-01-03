package com.sayam.papperdesigners;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;

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

public class TeacherLoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    EditText email,password;
    TextView makeAccount,reset;
    Button login,google;
    GoogleSignInClient client;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog dialog;
    AdView teacherAdView;
    InterstitialAd googleInterAd,teacherLoginInterAd,resetInterAd,makeInterAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);
        init();
        AdRequest request = new AdRequest.Builder().build();
        teacherAdView.loadAd(request);
        createRequest();
        makeAccount.setOnClickListener(view -> {
            if (makeInterAd !=null)
                makeInterAd.show(this);
            else {
                startActivity(new Intent(TeacherLoginActivity.this, TeacherSignInActivity.class));
                finish();
            }
        });
        login.setOnClickListener(v->{
            if (teacherLoginInterAd !=null){
                teacherLoginInterAd.show(this);
            }else {
                dialog.show();
                LogInWithEmailPassword();
            }
        });
        google.setOnClickListener(v-> {
            if (googleInterAd !=null)
                googleInterAd.show(this);
            else
                signIn();
        });
        reset.setOnClickListener(v->{
            if (resetInterAd !=null)
                resetInterAd.show(this);
            else
                startActivity(new Intent(this,ForgotPassword.class));
        });
        // google InterAd
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
                        signIn();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        googleInterAd = null;
                        Log.d(MainActivity.TAG, "onAdFailedToShowFullScreenContent: Error for not in full screen");
                    }
                });
            }
        });
        // login InterAd
        InterstitialAd.load(this, getString(R.string.INTER_AD_UNIT_ID), request, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.d(MainActivity.TAG, "onAdFailedToLoad: Error: "+loadAdError.getMessage());
                teacherLoginInterAd = null;
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                teacherLoginInterAd = interstitialAd;
                teacherLoginInterAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        dialog.show();
                        LogInWithEmailPassword();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        teacherLoginInterAd = null;
                        Log.d(MainActivity.TAG, "onAdFailedToShowFullScreenContent: Error for not in full screen");
                    }
                });
            }
        });
        // reset InterAd
        InterstitialAd.load(this, getString(R.string.INTER_AD_UNIT_ID), request, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.d(MainActivity.TAG, "onAdFailedToLoad: Error: "+loadAdError.getMessage());
                resetInterAd = null;
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                resetInterAd = interstitialAd;
                resetInterAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        startActivity(new Intent(TeacherLoginActivity.this,ForgotPassword.class));
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        resetInterAd = null;
                        Log.d(MainActivity.TAG, "onAdFailedToShowFullScreenContent: Error for not in full screen");
                    }
                });
            }
        });
        // make InterAd
        InterstitialAd.load(this, getString(R.string.INTER_AD_UNIT_ID)  , request, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.d(MainActivity.TAG, "onAdFailedToLoad: Error: "+loadAdError.getMessage());
                makeInterAd = null;
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                makeInterAd = interstitialAd;
                makeInterAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        startActivity(new Intent(TeacherLoginActivity.this, TeacherSignInActivity.class));
                        finish();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        makeInterAd = null;
                        Log.d(MainActivity.TAG, "onAdFailedToShowFullScreenContent: Error for not in full screen");
                    }
                });
            }
        });


    }
    private void LogInWithEmailPassword(){
        auth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                .addOnCompleteListener(task -> {
                    dialog.dismiss();
                   if (!task.isSuccessful()){
                       Toast.makeText(this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                   }else{
                       FirebaseUser users = auth.getCurrentUser();
                       UserModel userModel = new UserModel();
                       assert users != null;
                       userModel.setUserId(users.getUid());
                       userModel.setUserName(users.getDisplayName());
                       userModel.setUserEmail(users.getEmail());
                       database.getReference().child("Schools")
                               .child(users.getUid()).setValue(userModel);
                       startActivity(new Intent(TeacherLoginActivity.this, MainActivity.class));
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
        auth.signInWithCredential(credential)
                .addOnCompleteListener(task-> {
                    if (task.isSuccessful()) {
                        FirebaseUser users = auth.getCurrentUser();
                        UserModel userModel = new UserModel();
                        assert users != null;
                        userModel.setUserId(users.getUid());
                        userModel.setUserName(users.getDisplayName());
                        userModel.setUserEmail(users.getEmail());
                        database.getReference().child("Schools")
                                 .child(users.getUid()).setValue(userModel);
                        startActivity(new Intent(TeacherLoginActivity.this, MainActivity.class));
                        finish();
                    }
                });

    }
    @SuppressWarnings("deprecation")
    private void signIn(){
        Intent i = client.getSignInIntent();
        startActivityForResult(i,RC_SIGN_IN);
    }
    private void createRequest(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        client = GoogleSignIn.getClient(this,gso);
    }
    private void init(){
        teacherAdView = findViewById(R.id.teacherAdView);
        email = findViewById(R.id.et_emailId);
        password = findViewById(R.id.et_passwordId);
        makeAccount = findViewById(R.id.make_account);
        reset = findViewById(R.id.tv_reset_password);
        login = findViewById(R.id.btn_login);
        auth  = FirebaseAuth.getInstance();
        database  = FirebaseDatabase.getInstance();
        google = findViewById(R.id.google_logIn);
        dialog = new ProgressDialog(this);
        dialog.setTitle("LogIn...");
        dialog.setMessage("Logging you in...");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this,MainActivity.class));
        }
    }
}