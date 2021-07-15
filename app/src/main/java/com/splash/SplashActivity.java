package com.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.liberianpro.R;
import com.signin.SignInActivity;
import com.signup.SignUpActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int delay = 2000;
        new Handler().postDelayed(()->{
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        },delay);
    }
}