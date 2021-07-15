package com.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        recyclerView = findViewById(R.id.setting_list);
        List<Setting> settingList = new ArrayList<>();

        settingList.add(new Setting("Institute", "University of Bamenda"));
        settingList.add(new Setting("Email", "prologjmaster@gmail.com"));
        settingList.add(new Setting("Password","\u2022\u2022\u2022\u2022\u2022\u2022\u2022\u2022\u2022\u2022\u2022\u2022\u2022\u2022\u2022\u2022"));
        settingList.add(new Setting("License","Not available"));
        settingList.add(new Setting("Contact us","Questions? Need help?"));
        SettingsAdapter settingsAdapter = new SettingsAdapter(settingList);
        recyclerView.setAdapter(settingsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration( new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL));
    }
}