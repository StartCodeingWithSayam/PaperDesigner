package com.sayam.papperdesigners;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    EditText et_reset_password;
    Button  resetButton;
    FirebaseAuth auth;
     String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        init();
        email = et_reset_password.getText().toString();
        resetButton.setOnClickListener(v-> resetPassword());

    }

    private void resetPassword() {
        if (et_reset_password.length()<=0){
            et_reset_password.setError("Enter a email");
        }else {
            auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Toast.makeText(this, "Error " + task.getException(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "The reset link has been sent to your email", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void init(){
        et_reset_password = findViewById(R.id.et_reset_email);
        resetButton = findViewById(R.id.btn_reset_password);
        auth = FirebaseAuth.getInstance();
    }

}