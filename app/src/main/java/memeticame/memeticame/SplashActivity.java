package memeticame.memeticame;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import memeticame.memeticame.contacts.ContactsActivity;
import memeticame.memeticame.users.PhoneAuthActivity;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_NS = 2000;
    private Handler mHandler;
    private Runnable mRunnble;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mHandler = new Handler();
        mRunnble = new Runnable() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser()!=null){
                    //MainActivity.startActivity(SplashActivity.this);
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    //PhoneAuthActivity.startIntent(SplashActivity.this);
                    Intent intent = new Intent(SplashActivity.this, PhoneAuthActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        };

        mHandler.postDelayed(mRunnble, SPLASH_TIME_NS);

    }
}
