package com.signature;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.kyanogen.signatureview.SignatureView;
import com.liberian.auth.LiberianAuth;
import com.liberianpro.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SignatureActivity extends AppCompatActivity {

    String email="";

    SignatureView signatureView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        SharedPreferences preferences = getSharedPreferences("Liberian",MODE_PRIVATE);
        email = preferences.getString("email","");

        signatureView =  findViewById(R.id.signature_view);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_signature){
            Bitmap bitmap = signatureView.getSignatureBitmap();
            SaveTask task = new SaveTask(this,bitmap);
            task.execute();
            return true;
        }
        else if (item.getItemId() == R.id.delete_signature){
            signatureView.clearCanvas();
            return true;
        }
        return false;
    }

    class SaveTask extends AsyncTask<Void,Void,Void>{


        Context context;
        Bitmap bitmap;
        String result;
        ProgressDialog progressDialog;
        AlertDialog alertDialog;
        public SaveTask(Context context, Bitmap bitmap) {
            this.context = context;
            this.bitmap = bitmap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Saving signature...");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(false);
            progressDialog.show();

            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"Okay",(dialog, which) -> {
                dialog.dismiss();
            });

            File file = new File(Environment.getExternalStorageDirectory().toString()+"/Liberian Pro");
            if (!file.exists()){
                file.mkdir();
            }
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            progressDialog.dismiss();
            if ("Success".equals(result)){
                alertDialog.setMessage("Signature saved");
                alertDialog.show();
            }
            else{
                alertDialog.setMessage("Check your internet connection or Failed to access storage");
                alertDialog.show();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            File file = new File(Environment.getExternalStorageDirectory().toString()+"/Liberian Pro/signature.png");
            try(FileOutputStream outputStream = new FileOutputStream(file)){
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            }catch (IOException e){

            }

            try {
                File file1 = new File(Environment.getExternalStorageDirectory().toString()+"/Liberian Pro/signature.png");
                result = LiberianAuth.saveSignature(file1,email);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}