package com.signature;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.kyanogen.signatureview.SignatureView;
import com.liberianpro.R;

public class SignatureActivity extends AppCompatActivity {


    SignatureView signatureView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        signatureView =  findViewById(R.id.signature_view);
        Bitmap bitmap = signatureView.getSignatureBitmap();

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
            return true;
        }
        else if (item.getItemId() == R.id.delete_signature){
            signatureView.clearCanvas();
            return true;
        }
        return false;
    }
}