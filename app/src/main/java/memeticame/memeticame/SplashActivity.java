package memeticame.memeticame;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

import memeticame.memeticame.users.EmailPasswordAuth;
import memeticame.memeticame.users.PhoneAuthActivity;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_NS = 2000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler mHandler = new Handler();
        Runnable mRunnble = () -> {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                startActivity(MainActivity.getIntent(SplashActivity.this));

            } else {
                startActivity(EmailPasswordAuth.getIntent(SplashActivity.this));
            }
            finish();
        };

        mHandler.postDelayed(mRunnble, SPLASH_TIME_NS);

    }
}
