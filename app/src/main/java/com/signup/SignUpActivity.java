package com.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.liberianpro.MainActivity;
import com.liberianpro.R;
import com.signin.SignInActivity;

public class SignUpActivity extends AppCompatActivity {

    TextInputEditText nameOfInstitute;
    TextInputEditText email;
    TextInputEditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nameOfInstitute = findViewById(R.id.name_signup);
        email = findViewById(R.id.email_signup);
        password = findViewById(R.id.pwd_signup);
    }

    public void signin(View view){
        startActivity(new Intent(this, SignInActivity.class));
    }

    public void createAccount(View view){

        String nameOfInstituteString = nameOfInstitute.getText().toString();
        String emailString = email.getText().toString();
        String passwordString = password.getText().toString();
        startActivity(new Intent(this, MainActivity.class));
    }


}