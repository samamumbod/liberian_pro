package com.liberianpro;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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
    }


}