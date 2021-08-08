package com.liberianpro;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<Option> options = new ArrayList<>();
        options.add(new Option(R.drawable.record_book_24,"Record book"));
        options.add(new Option(R.drawable.issue_book_24,"Issue book"));
        options.add(new Option(R.drawable.return_book_24,"Return book"));
        options.add(new Option(R.drawable.catergory_24,"Category"));
        options.add(new Option(R.drawable.catergory_24,"School"));
        options.add(new Option(R.drawable.signature_24,"Signature"));
        options.add(new Option(R.drawable.setting,"Settings"));
        options.add(new Option(R.drawable.logout_24,"Log out"));
        recyclerView = findViewById(R.id.recycler);
        MainAdapter mainAdapter = new MainAdapter(options);
        recyclerView.setAdapter(mainAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));


        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


}