package com.example.loginotpdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginNumberInputScreen extends AppCompatActivity {
EditText name,no;
Button getOtp;
ProgressBar progressBar;
FirebaseAuth mAuth;
PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_number_input_screen);
        name=findViewById(R.id.editTextName);
        no=findViewById(R.id.editTextPhoneNo);
        getOtp=findViewById(R.id.buttonGetOtp);
        progressBar=findViewById(R.id.progressBar);

        mAuth=FirebaseAuth.getInstance();

        getOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(!no.getText().toString().trim().isEmpty()){
                if (no.getText().toString().trim().length()==10){

                    sendOtp();

                }
            }
            }
        });
    }

    private void sendOtp() {
        progressBar.setVisibility(View.VISIBLE);
        getOtp.setVisibility(View.GONE);
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
            progressBar.setVisibility(View.GONE);
            getOtp.setVisibility(View.VISIBLE);
                Toast.makeText(LoginNumberInputScreen.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                progressBar.setVisibility(View.GONE);
                getOtp.setVisibility(View.VISIBLE);
                Intent i=new Intent(LoginNumberInputScreen.this,LoginOtpScreen.class);
                i.putExtra("num",no.getText().toString());
                i.putExtra("otpCode",verificationId);
                startActivity(i);
            }
        };
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91"+no.getText().toString().trim())       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}