package com.signature;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;

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
}