package com.example.otptestapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String verificationId;
    String phone;


    @BindView(R.id.idEdtPhoneNumber) EditText etPhoneNumber;
//    @BindView(R.id.idEdtOtp) EditText etOtp;
    @BindView(R.id.idBtnGetOtp) Button btnGenerateOtp;
//    @BindView(R.id.idBtnVerify) Button btnVerifyOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        btnGenerateOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // below line is for checking weather the user
                // has entered his mobile number or not.
                if(TextUtils.isEmpty(etPhoneNumber.getText().toString())){
                    Toast.makeText(MainActivity.this,"Please enter a valid phone number",Toast.LENGTH_LONG).show();
                }else{
                        phone = "+254" + etPhoneNumber.getText().toString();
                        sendVerificationCode(phone);
                }

            }
        });

        // initializing on click listener
        // for verify otp button
//        btnVerifyOtp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (TextUtils.isEmpty(etOtp.getText().toString())){
//                    Toast.makeText(MainActivity.this,"Please enter a valid phone number",Toast.LENGTH_LONG).show();
//
//                }else{
//                    verifyCode(etOtp.getText().toString());
//                }
//            }
//        });




    }
//    private void signInWithCredential(PhoneAuthCredential credential) {
//        // inside this method we are checking if the code entered is correct or not.
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // if the code is correct and the task is successful we are sending our user to new activity.
//                            Intent i = new Intent(MainActivity.this, HomeActivity.class);
//                            startActivity(i);
//                            finish();
//                        } else {
//                            // if the code is not correct then we are
//                            // displaying an error message to the user.
//                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//    }


    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number, // first parameter is user's mobile number
                60, // second parameter is time limit for OTP verification which is 60 seconds in our case.
                TimeUnit.SECONDS, // third parameter is for initializing units for time period which is in seconds in our case.
                MainActivity.this,// this task will be executed on Main thread.
                mCallbacks // we are calling callback method when we receive OTP for auto verification of user.
        );
    }

    // callback method is called on Phone auth provider.
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on verification callback method.
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it contains a unique id which we are storing in our string which we have already created.
            verificationId = s;
            Intent intent = new Intent(MainActivity.this,VerifyOTP.class);
            intent.putExtra("phoneNo",etPhoneNumber.getText().toString());
            intent.putExtra("verificationId",s);
            startActivity(intent);
        }

        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code which is sent in phone auth credentials.
            //final String code = phoneAuthCredential.getSmsCode();


        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    // below method is use to verify code from Firebase.
//    private void verifyCode(String code) {
//        // below line is used for getting getting
//        // credentials from our verification id and code.
//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
//
//        // after getting credential we are
//        // calling sign in method.
//        signInWithCredential(credential);
//    }

}