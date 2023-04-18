package com.example.loginotpdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class LoginOtpScreen extends AppCompatActivity {
    PinView pinView;
    TextView no, resend;
    Button verify;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp_screen);
        getSupportActionBar().hide();
        pinView = findViewById(R.id.firstPinView);
        no=findViewById(R.id.textView6);
        resend=findViewById(R.id.textViewResend);
        verify=findViewById(R.id.buttonVerifyOtp);
        progressBar=findViewById(R.id.progressBar);

        no.setText("+91"+getIntent().getStringExtra("num").trim());
        no.setVisibility(View.VISIBLE);
        String otp=getIntent().getStringExtra("otpCode");

        pinView.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginOtpScreen.this, "SUCCESSFULLY SENT", Toast.LENGTH_SHORT).show();
            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                verify.setVisibility(View.GONE);
                if(pinView.getText().toString().isEmpty()){
                    Toast.makeText(LoginOtpScreen.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(otp!=null){
                        String code= pinView.getText().toString();
                        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(otp,code);
                        FirebaseAuth.getInstance().signInWithCredential(credential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                       if(task.isSuccessful()){
                                           Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                           intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                           startActivity(intent);
                                       }
                                       else {
                                           progressBar.setVisibility(View.GONE);
                                           verify.setVisibility(View.VISIBLE);
                                           Toast.makeText(LoginOtpScreen.this, "OTP not valid", Toast.LENGTH_SHORT).show();
                                       }
                                    }
                                });
                    }
                }
            }
        });

    }

}