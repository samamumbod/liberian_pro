package com.signin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.liberian.auth.LiberianAuth;
import com.liberianpro.MainActivity;
import com.liberianpro.R;
import com.signup.SignUpActivity;

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

    public void signup(View view){
        startActivity(new Intent(this, SignUpActivity.class));
    }

    public void forgotPassword(View view){
        Toast.makeText(this,"Pending",Toast.LENGTH_SHORT).show();
    }

    public void login(View view){
        String emailString = email.getText().toString();
        String passwordString = password.getText().toString();
//        SignInTask task = new SignInTask(emailString,passwordString);
//        task.execute();
        startActivity(new Intent(this, MainActivity.class));

    }

    class SignInTask extends AsyncTask<Void,Void,Void>{
        AlertDialog dialog;
        String email;
        String pwd;
        String result;

        public SignInTask(String email, String pwd) {
            this.email = email;
            this.pwd  = pwd;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            dialog = new AlertDialog.Builder(SignInActivity.this).create();
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            progressBar.setVisibility(View.INVISIBLE);
            if (!result.isEmpty() && result !=null){
                if (result.equals("error")) {
                    dialog.setMessage("Ooops network problem.");
                    dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                else{
                    if (pwd.equals(result)){
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        intent.putExtra("email",email);
                        startActivity(intent);
                    }
                    else{
                        password.setError("Invalid password");
                    }
                }
            }
            else{
                dialog.setMessage("Email does not exist.");
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {

            result = LiberianAuth.signin(email);
            return null;
        }
    }
}