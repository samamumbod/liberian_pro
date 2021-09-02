package com.record_book;

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
import com.liberianpro.R;
import com.update_book.UpdateBookActivity;

import java.io.IOException;
import java.lang.reflect.Field;


public class RecordBookActivity extends AppCompatActivity {

    String updateStatus;
    boolean isOn = true;
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


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_interface);
        cameraView = findViewById(R.id.surfaceView3);
        bookInfo = findViewById(R.id.textView19);
        onTorch = findViewById(R.id.floatingActionButton3);
        clearButton = findViewById(R.id.button8);
        nextButton = findViewById(R.id.button9);

        updateStatus = getIntent().getExtras().getString("update");

        startCameraSource();

        onTorch.setOnClickListener(v -> {
            if (isOn) {
                onTorch.setImageDrawable(getDrawable(R.drawable.ic_baseline_flash_on_24));
                if (camera != null) {
                    Camera.Parameters p = camera.getParameters();
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(p);
                    camera.startPreview();
                }
                isOn = false;
            } else {
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
        });

        nextButton.setOnClickListener(v -> {
            if ("no".equals(updateStatus)){
                if (!bookInfo.getText().toString().isEmpty()){
                    Intent intent = new Intent(RecordBookActivity.this, FinalRecordActivity.class);
                    intent.putExtra("isbn",bookInfo.getText().toString());
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(v.getContext(), FinalRecordActivity.class);
                    startActivity(intent);
                }
            }
            else{
                if (!bookInfo.getText().toString().isEmpty()){
                    Intent intent = new Intent(RecordBookActivity.this, UpdateBookActivity.class);
                    intent.putExtra("isbn",bookInfo.getText().toString());
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(v.getContext(),UpdateBookActivity.class);
                    startActivity(intent);
                }
            }
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


    private void startCameraSource(){
        barcodeDetector =
                new BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.ALL_FORMATS)
                        .build();

        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080).setAutoFocusEnabled(true)
                .build();


        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                try {
                    if ( ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                    }
                    if (cameraSource!=null){
                        cameraSource.start(holder);
                        camera = getCamera(cameraSource);
                    }
                } catch (IOException ie) {
                    System.err.println(ie);
//                    camera.release();
//                    camera=null;
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
//                if (cameraSource!=null){
//                    cameraSource.release();
//                    cameraSource = null;
//                }
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
                    toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                    // Use the post method of the TextView
                    bookInfo.post(() -> {
                        if (barcodes.valueAt(0).displayValue.matches("[0-9]{10,13}")) {
                            bookInfo.setText(barcodes.valueAt(0).displayValue);
                        }
                    });
                    toneGen1.stopTone();
                }
            }

        });
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
////        if (cameraSource !=null){
////            cameraSource.release();
////            cameraSource = null;
////        }
//    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
//        if (camera!=null){
//            camera.stopPreview();
//            camera=null;
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCameraSource();
    }

}
