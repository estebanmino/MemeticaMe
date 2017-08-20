package memeticame.memeticame;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.concurrent.TimeUnit;

public class PhoneAuthActivity extends AppCompatActivity {


    EditText EditPhone, EditCode;
    Button SubmitPhone, SubmitCode, SignOut;
    TextView TextPhone, TextCode;

    // [START declare_auth]
    FirebaseAuth mAuth;
    // [END declare_auth]

    boolean mVerificationInProgress = false;
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);

        EditPhone = (EditText) findViewById(R.id.edit_phone);
        SubmitPhone = (Button) findViewById(R.id.submit_phone);
        EditCode = (EditText) findViewById(R.id.edit_code);
        SubmitCode = (Button) findViewById(R.id.submit_code);

        SignOut = (Button) findViewById(R.id.log_out);

        TextPhone = (TextView) findViewById(R.id.text_phone);
        TextCode = (TextView) findViewById(R.id.text_code);

        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
                //Log.d("", "onVerificationCompleted:" + credential);

                Toast.makeText(PhoneAuthActivity.this,"Verification completed",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                //Log.w(TAG, "onVerificationFailed", e);
                Toast.makeText(PhoneAuthActivity.this,"Verification failed",Toast.LENGTH_LONG).show();

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(PhoneAuthActivity.this,"Invalid Phone number",Toast.LENGTH_LONG).show();
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Toast.makeText(PhoneAuthActivity.this,"SMS quote over",Toast.LENGTH_LONG).show();
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                //Log.d(TAG, "onCodeSent:" + verificationId);
                Toast.makeText(PhoneAuthActivity.this,"Verification code sent",Toast.LENGTH_LONG).show();

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                EditPhone.setVisibility(View.GONE);
                SubmitPhone.setVisibility(View.GONE);
                TextPhone.setVisibility(View.GONE);

                EditCode.setVisibility(View.VISIBLE);
                SubmitCode.setVisibility(View.VISIBLE);
                TextCode.setVisibility(View.VISIBLE);
                // ...
            }
        };


        SubmitPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validatePhoneNumber(EditPhone.getText().toString())) {
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            EditPhone.getText().toString(),
                            60,
                            TimeUnit.SECONDS,
                            PhoneAuthActivity.this,
                            mCallbacks
                    );
                }
            }
        });

        SubmitCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(
                        mVerificationId,
                        EditCode.getText().toString()
                );
                signInWithPhoneAuthCredential(credential);
            }
        });

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(PhoneAuthActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(PhoneAuthActivity.this,"Verification done",Toast.LENGTH_LONG).show();

                            @SuppressWarnings("UnusedAssignment") FirebaseUser user = task.getResult().getUser();
                            Intent intent = new Intent(PhoneAuthActivity.this, MainActivity.class);
                            startActivity(intent);
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(PhoneAuthActivity.this,"Verification failed code invalid",Toast.LENGTH_LONG).show();

                            }
                        }
                    }
                });
    }

    private boolean validatePhoneNumber(String phoneNumber) {
        return !TextUtils.isEmpty(phoneNumber);
    }

    private void signOut() {
        mAuth.signOut();
        //updateUI(STATE_INITIALIZED);
    }
}
