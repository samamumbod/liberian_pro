package com.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.liberianpro.R;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SharedPreferences preferences = getSharedPreferences("Liberian",MODE_PRIVATE);

        recyclerView = findViewById(R.id.setting_list);
        List<Setting> settingList = new ArrayList<>();

        settingList.add(new Setting("Institute", preferences.getString("institute","")));
        settingList.add(new Setting("Email", preferences.getString("email","")));
        settingList.add(new Setting("Contact us","Questions? Need help?"));
        SettingsAdapter settingsAdapter = new SettingsAdapter(settingList);
        recyclerView.setAdapter(settingsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration( new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL));
    }
}