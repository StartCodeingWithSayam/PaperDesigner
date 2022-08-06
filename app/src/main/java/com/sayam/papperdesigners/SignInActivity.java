package com.sayam.papperdesigners;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class SignInActivity extends AppCompatActivity {
    EditText name,email,password;
    Button register;
    TextView alreadyHaveAccount;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog dialog;
    String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        init();
        result = password.getText().toString();
        Encrypt(result);
        alreadyHaveAccount.setOnClickListener(view -> startActivity(new Intent(SignInActivity.this,LoginActivity.class)));
        register.setOnClickListener(v->{
            dialog.show();
            SignInWithEmailPassword();
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
               database.getReference().child("Users").child(id).setValue(userModel);
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
        name = findViewById(R.id.et_name);
        email = findViewById(R.id.et_emailid);
        password = findViewById(R.id.et_passwordid);
        register = findViewById(R.id.btn_register);
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setTitle("SignUp");
        dialog.setMessage("We are SignIn you in..");
        database = FirebaseDatabase.getInstance();
        alreadyHaveAccount = findViewById(R.id.already_have_account);

    }
}