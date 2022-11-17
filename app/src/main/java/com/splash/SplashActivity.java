package com.splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.liberianpro.MainActivity;
import com.liberianpro.R;
import com.signin.SignInActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int delay = 2000;
        SharedPreferences preferences = getSharedPreferences("Liberian",MODE_PRIVATE);

        if ("main".equals(preferences.getString("activity",""))){
            new Handler().postDelayed(()->{
                startActivity(new Intent(this, MainActivity.class));
                finish();
            },delay);
        }
        else if ("signin".equals(preferences.getString("activity",""))){
            new Handler().postDelayed(()->{
                startActivity(new Intent(this, SignInActivity.class));
                finish();
            },delay);
        }
        else{
            new Handler().postDelayed(()->{
                startActivity(new Intent(this, SignInActivity.class));
                finish();
            },delay);
        }
    }
}