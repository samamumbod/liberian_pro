package com.return_book;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.issue_book.FinalIssueActivity;
import com.liberianpro.R;

import java.io.IOException;
import java.lang.reflect.Field;

public class ReturnBookActivity extends AppCompatActivity {

    boolean isOn= true;
    private static final int MY_CAMERA_REQUEST_CODE = 101;
    ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);

    Camera camera;
    SurfaceView cameraView;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;

    Button clearButton;
    Button nextButton;
    FloatingActionButton onTorch;
    TextView bookInfo;
    TextView regNumberInfo;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_book);
        cameraView = findViewById(R.id.surfaceView2);
        bookInfo = findViewById(R.id.textView15);
        regNumberInfo = findViewById(R.id.textView17);
        onTorch = findViewById(R.id.floatingActionButton2);
        clearButton = findViewById(R.id.button3);
        nextButton = findViewById(R.id.button5);

        barcodeDetector =
                new BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.ALL_FORMATS)
                        .build();

        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080).setAutoFocusEnabled(true)
                .build();

        CameraSource.class.getDeclaredFields();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                try {
                    if ( ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                    }
                    cameraSource.start(holder);
                    camera = getCamera(cameraSource);
                } catch (IOException ie) {
                    System.err.println(ie);
                    camera.release();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.release();
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,150);
                    // Use the post method of the TextView
                    bookInfo.post(() -> {
                        if (barcodes.valueAt(0).displayValue.matches("[0-9]{13}")){
                            bookInfo.setText(barcodes.valueAt(0).displayValue);
                        }
                    } );

                    regNumberInfo.post(() -> {
                        if (barcodes.valueAt(0).displayValue.matches("[A-Za-z0-9]+")){
                            regNumberInfo.setText("");
                        }
                    });
                    toneGen1.stopTone();
                }
            }
        });


        onTorch.setOnClickListener(v -> {
            if (isOn){
                onTorch.setImageDrawable(getDrawable(R.drawable.ic_baseline_flash_on_24));
                if (camera != null){
                    Camera.Parameters p = camera.getParameters();
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(p);
                    camera.startPreview();
                }
                isOn = false;
            }
            else{
                onTorch.setImageDrawable(getDrawable(R.drawable.ic_baseline_flash_off_24));
                Camera.Parameters p = camera.getParameters();
                p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(p);
                camera.startPreview();
                isOn = true;
            }
        });

        clearButton.setOnClickListener(v -> {
            bookInfo.setText("");
            regNumberInfo.setText("");
        });

        nextButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), FinalReturnActivity.class);
            startActivity(intent);
        });

    }


    public Camera getCamera(CameraSource cameraSource) {
        Field[] declaredFields = CameraSource.class.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.getType() == Camera.class) {
                field.setAccessible(true);
                try {
                    Camera camera = (Camera) field.get(cameraSource);
                    if (camera != null) {
                        return camera;
                    }
                    return null;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return null;
    }
}