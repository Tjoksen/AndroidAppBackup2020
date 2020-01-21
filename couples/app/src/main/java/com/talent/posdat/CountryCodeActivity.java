package com.talent.posdat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.talent.posdat.home.HomeActivity;

import java.util.concurrent.TimeUnit;

public class CountryCodeActivity extends AppCompatActivity {

    private  static final  String TAG= "PhoneAuth";
    private EditText phoneText;
    private EditText codeText;
    private Button verifyButton;
    private  Button sendButton;
    private  Button resendButton;
    private Button signoutButton;
    private TextView statusText;
            String number;
    private String phoneVerificationId;
    private  PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;

    private FirebaseAuth fbAuth;
    CountryCodePicker ccp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_code);

        phoneText= findViewById(R.id.phoneText);
        codeText= findViewById(R.id.codeText);
        verifyButton=findViewById(R.id.verifyButton);
        sendButton=findViewById(R.id.sendButton);
        resendButton=findViewById(R.id.resendButton);
        signoutButton=findViewById(R.id.signoutButton);
        statusText=findViewById(R.id.statusText);

        ccp=findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(phoneText);

        verifyButton.setEnabled(false);
        resendButton.setEnabled(false);
        signoutButton.setEnabled(false);
        statusText.setText("Signed Out");

        fbAuth=FirebaseAuth.getInstance();


    }



    public void sendCode(View view)
    {
        number=ccp.getFullNumberWithPlus();
        setUpVerificationCallbacks();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(number,//Phone number to verify
                60,                                             //Timeout duration
                TimeUnit.SECONDS,                                   //Unit of timeout
                this  ,                                     //Activity(for callback binding)
                verificationCallbacks

                );

    }

    private void setUpVerificationCallbacks()
    {
        verificationCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential)
            {
                signoutButton.setEnabled(true);
                statusText.setText("Signed In");
                resendButton.setEnabled(false);
                verifyButton.setEnabled(false);
                codeText.setText("");
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                if(e instanceof FirebaseAuthInvalidCredentialsException)
                {
                    //Invalid request
                    Log.d(TAG,"Invalid credential" + e.getLocalizedMessage());
                }
               else if(e instanceof FirebaseTooManyRequestsException)
                {
                    //SMS quota exceeded
                    Log.d(TAG,"SMS Quota exceeded" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                phoneVerificationId=verificationId;
                resendToken=forceResendingToken;
                verifyButton.setEnabled(true);
                sendButton.setEnabled(false);
                resendButton.setEnabled(true);

            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential)
    {
        fbAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            signoutButton.setEnabled(true);
                            codeText.setText("");
                            statusText.setText("Signed In");
                            resendButton.setEnabled(false);
                            verifyButton.setEnabled(false);
                            FirebaseUser user= task.getResult().getUser();
                            String phoneNumber= user.getPhoneNumber();

                            Intent intent= new Intent(CountryCodeActivity.this, HomeActivity.class);
                             intent.putExtra("phone",phoneNumber);
                             startActivity(intent);
                             finish();
                        }else {
                            if(task.getException() instanceof  FirebaseAuthInvalidCredentialsException)
                            {
                            //The verification code entered is invalid
                            }
                        }


                    }
                });
    }

    public void verifyCode(View view)
    {
        String code= codeText.getText().toString();
        PhoneAuthCredential credential= PhoneAuthProvider.getCredential(phoneVerificationId,code);
        signInWithPhoneAuthCredential(credential);
    }

    public void resendCode(View view)
    {
        number=ccp.getFullNumberWithPlus();
        setUpVerificationCallbacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS
                ,
                this,
                verificationCallbacks,
                resendToken
        );
    }

    public void signOut(View view)
    {
        fbAuth.signOut();
        statusText.setText("Signed Out");
        signoutButton.setEnabled(true);
        sendButton.setEnabled(true);

    }
}
