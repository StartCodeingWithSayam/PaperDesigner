package com.sayam.papperdesigners;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        createRequest();
        makeAccount.setOnClickListener(view -> {
            startActivity(new Intent(TeacherLoginActivity.this, TeacherSignInActivity.class));
            finish();
        });
        login.setOnClickListener(v->{
            dialog.show();
            LogInWithEmailPassword();
        });
        google.setOnClickListener(v-> signIn());
        reset.setOnClickListener(v->startActivity(new Intent(this,ForgotPassword.class)));
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