package com.school;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.liberian.auth.LiberianAuth;
import com.liberian.auth.SchoolJson;
import com.liberianpro.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SchoolActivity extends AppCompatActivity {

    static String tableName="";
    static SchoolAdapter schoolAdapter;
    static RecyclerView recyclerView;
    static ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school);
        recyclerView = findViewById(R.id.sch_recycler);
        progressBar = findViewById(R.id.progressBar2);

        SharedPreferences preferences = getSharedPreferences("Liberian",MODE_PRIVATE);
        tableName = preferences.getString("table","");

        RetrieveSchoolTask task = new RetrieveSchoolTask(SchoolActivity.this);
        task.execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_category,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.refresh){
            RetrieveSchoolTask task = new RetrieveSchoolTask(SchoolActivity.this);
            task.execute();
            return true;
        }
        else if (item.getItemId() == R.id.add) {
            addItem();
            return true;
        }
        return false;
    }


    public void addItem(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.layout_school_dialog,null);
        dialog.setTitle("School");
        dialog.setView(view);
        EditText meaningText  = view.findViewById(R.id.editTextTextPersonName);
        EditText schoolText = view.findViewById(R.id.editTextTextPersonName2);

        dialog.setPositiveButton("Okay", (dialog1, which) -> {

            String school = schoolText.getText().toString();
            String meaning = meaningText.getText().toString();

            if (school.isEmpty() && meaning.isEmpty()){

            }
            else{
                AddSchoolTask task = new AddSchoolTask(SchoolActivity.this,school,meaning);
                task.execute();
            }
        }).setNegativeButton("Cancel", (dialog12, which) -> dialog12.dismiss());
        dialog.show();

    }


    static class RetrieveSchoolTask extends AsyncTask<Void,Void,Void> {
        List<SchoolJson> list;
        String result="";
        Context context;

        public RetrieveSchoolTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                list = LiberianAuth.retrieveSchool(tableName);
            } catch (IOException e) {
                result = "error";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            progressBar.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);

            if (result.equals("error")){
                Toast.makeText(context,"Ooops network problem.",Toast.LENGTH_SHORT).show();
            }
            else{
                if (list!=null) {
                    schoolAdapter = new SchoolAdapter(list);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setAdapter(schoolAdapter);
                    recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL));
                }
            }
        }
    }


    static class AddSchoolTask extends AsyncTask<Void,Void,Void>{
        String result="";
        String school;
        String meaning;

        Context context;

        public AddSchoolTask(Context context, String school, String meaning) {
            this.context = context;
            this.school = school;
            this.meaning = meaning;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(context,"Adding School",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                result = LiberianAuth.addSchool(tableName,school,meaning);
            } catch (IOException e) {
                result = "error";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            switch (result) {
                case "Success":
                    Toast.makeText(context,"School added Successfully",Toast.LENGTH_SHORT).show();
                    RetrieveSchoolTask task = new RetrieveSchoolTask(context);
                    task.execute();
                    break;
                case "Failed":
                    Toast.makeText(context,"School already exist",Toast.LENGTH_SHORT).show();
                    break;
                case "error":
                    Toast.makeText(context,"Ooops network error.",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    static void restart(Context context){
        RetrieveSchoolTask task = new RetrieveSchoolTask(context);
        task.execute();
    }

}