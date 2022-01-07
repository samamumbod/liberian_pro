package com.signature;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.kyanogen.signatureview.SignatureView;
import com.liberian.auth.LiberianAuth;
import com.liberianpro.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import kotlin.ranges.UIntRange;

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
            progressDialog.setMessage("Processing signature...");
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

            File file = new File(Environment.getExternalStorageDirectory().toString()+"/Liberian Pro/signature.png");
            Uri uri = Uri.fromFile(file);
            CropImage.activity(uri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(SignatureActivity.this);
        }


        @Override
        protected Void doInBackground(Void... voids) {
            File file = new File(Environment.getExternalStorageDirectory().toString()+"/Liberian Pro/signature.png");
            try(FileOutputStream outputStream = new FileOutputStream(file)){
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            }catch (IOException e){

            }
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                UploadTask task = new UploadTask(this,resultUri);
                task.execute();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    class UploadTask extends AsyncTask<Void,Void,Void>{
        Context context;
        String result;
        ProgressDialog progressDialog;
        AlertDialog alertDialog;
        Uri resultUri;

        public UploadTask(Context context, Uri resultUri) {
            this.context = context;
            this.resultUri = resultUri;
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
            File file = new File(resultUri.getPath());
            File file1 = new File(Environment.getExternalStorageDirectory().toString()+"/Liberian Pro/signature.png");

            FileInputStream in = null;
            FileOutputStream out = null;

            try{
                in = new FileInputStream(file);
                out = new FileOutputStream(file1);
                int c;
                while ((c = in.read())!=-1){
                    out.write(c);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try{
                    if (in!=null){
                        in.close();
                        in=null;
                    }
                    if (out!=null){
                        out.close();
                        out = null;
                    }
                }catch (Exception e ){

                }
                try {
                    File file2 = new File(Environment.getExternalStorageDirectory().toString()+"/Liberian Pro/signature.png");
                    result = LiberianAuth.saveSignature(file2,email);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            return null;
        }
    }



}