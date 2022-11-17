package com.signin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.liberian.auth.LiberianAuth;
import com.liberian.auth.UserDetail;
import com.liberianpro.MainActivity;
import com.liberianpro.R;

import java.io.IOException;

public class SignInActivity extends AppCompatActivity {

    TextInputEditText email;
    TextInputEditText password;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        email = findViewById(R.id.email_signin);
        password = findViewById(R.id.pwd_signin);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void forgotPassword(View view){
        Toast.makeText(this,"Not available",Toast.LENGTH_SHORT).show();
    }

    public void login(View view){
        String emailString = email.getText().toString();
        String passwordString = password.getText().toString();
        SignInTask task = new SignInTask(emailString,passwordString);
        task.execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    class SignInTask extends AsyncTask<Void,Void,Void>{
        AlertDialog dialog = null;
        String email;
        String pwd;
        String result;
        UserDetail userDetail;

        public SignInTask(String email, String pwd) {
            this.email = email;
            this.pwd  = pwd;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            dialog = new AlertDialog.Builder(SignInActivity.this).create();
            dialog.setMessage("Email does not exist.");
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Okay", (dialog, which) -> dialog.dismiss());
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            progressBar.setVisibility(View.INVISIBLE);
            if (result !=null && !result.isEmpty()){
                if (result.equals("error")) {
                    dialog.setMessage("Ooops network problem.");
                    dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Okay", (dialog, which) -> dialog.dismiss());
                    dialog.show();
                }
                else{
                    if (pwd.equals(result)){
                        SharedPreferences preferences = getSharedPreferences("Liberian",MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("email",userDetail.getEmail());
                        editor.putString("institute",userDetail.getInstitute());
                        editor.putString("table",userDetail.getTable());
                        editor.putString("activity","main");
                        editor.apply();
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else{
                        password.setError("Invalid password");
                    }
                }
            }
            else{
               SignInActivity.this.email.setError("Email does not exist");
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                userDetail = LiberianAuth.signin(email);
                if (userDetail==null){
                    result = "";
                } else{
                    result = userDetail.getPassword();
                }
            } catch (IOException e) {
                result="error";
            }
            return null;
        }
    }
}